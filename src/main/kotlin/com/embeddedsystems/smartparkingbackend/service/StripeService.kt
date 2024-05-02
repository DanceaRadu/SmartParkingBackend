package com.embeddedsystems.smartparkingbackend.service

import com.embeddedsystems.smartparkingbackend.dto.PaymentInfo
import com.embeddedsystems.smartparkingbackend.entity.UserProfile
import com.embeddedsystems.smartparkingbackend.exceptions.types.BadRequestException
import com.embeddedsystems.smartparkingbackend.exceptions.types.EntityNotFoundByNameException
import com.embeddedsystems.smartparkingbackend.exceptions.types.EntityNotFoundException
import com.embeddedsystems.smartparkingbackend.repository.SubscriptionRepository
import com.embeddedsystems.smartparkingbackend.repository.UserProfileRepository
import com.stripe.model.Customer
import com.stripe.model.Event
import com.stripe.model.Subscription
import com.stripe.net.Webhook
import com.stripe.param.CustomerCreateParams
import com.stripe.param.SubscriptionCreateParams
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class StripeService(
    private val userProfileRepository: UserProfileRepository,
    private val subscriptionRepository: SubscriptionRepository
) {

    @Value("\${stripe.webhook-secret}")
    lateinit var endpointSecret: String

    fun createSubscriptionIntent(paymentInfo: PaymentInfo, keycloakId: String): String {
        val userProfile = userProfileRepository.findByKeycloakId(keycloakId)
            ?: throw EntityNotFoundByNameException("User Profile", keycloakId)

        checkIfSubscriptionCanBeCreated(userProfile, paymentInfo.licensePlate)

        val metadata = mapOf(
            "license_plate" to paymentInfo.licensePlate,
            "user_id" to userProfile.id.toString()
        )

        val subscriptionParams = SubscriptionCreateParams.builder()
            .setCustomer(userProfile.stripeCustomerId)
            .addItem(
                SubscriptionCreateParams.Item.builder()
                    .setPrice(paymentInfo.priceId)
                    .build()
            )
            .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
            .setPaymentSettings(
                SubscriptionCreateParams.PaymentSettings.builder()
                    .setSaveDefaultPaymentMethod(SubscriptionCreateParams.PaymentSettings.SaveDefaultPaymentMethod.ON_SUBSCRIPTION)
                    .build()
            )
            .addExpand("latest_invoice.payment_intent")
            .putAllMetadata(metadata)
            .build()

        val subscription =  Subscription.create(subscriptionParams)
        subscriptionRepository.save(com.embeddedsystems.smartparkingbackend.entity.Subscription(
            id = 0,
            userProfile = userProfile,
            licencePlate = paymentInfo.licensePlate,
            isActive = false,
            stripePriceId = paymentInfo.priceId,
            stripeSubscriptionId = subscription.id,
        ))

        return subscription.latestInvoiceObject.paymentIntentObject.clientSecret
    }

    fun createStripeCustomer(userProfile: UserProfile): String {
        val customerParams = CustomerCreateParams.builder()
            .setEmail(userProfile.email)
            .setName(userProfile.username)
            .build()

        return Customer.create(customerParams).id
    }

    fun handleSubscriptionUpdates(payload: String, request: HttpServletRequest): ResponseEntity<String> {
        val sigHeader = request.getHeader("Stripe-Signature")

        val event: Event = try {
            Webhook.constructEvent(payload, sigHeader, endpointSecret)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: ${e.message}")
        }

        when (event.type) {
            "customer.subscription.created" -> {
                val subscription = event.dataObjectDeserializer.`object`.orElse(null) as? Subscription
                if (subscription != null) {
                    println("Created subscription")
                } else {
                    return ResponseEntity.ok("Event received but subscription object was null or of unexpected type")
                }
            }
            "customer.subscription.deleted" -> {
                val subscription = event.dataObjectDeserializer.`object`.orElse(null) as? Subscription
                if (subscription != null) {
                    val localSubscription = getLocalSubscriptionFromStripeSubscription(subscription)
                        ?: return ResponseEntity.ok("Event received but local subscription was not found")
                    subscriptionRepository.deleteById(localSubscription.id)
                } else {
                    return ResponseEntity.ok("Event received but subscription object was null or of unexpected type")
                }
            }
            "customer.subscription.updated" -> {
                val subscription = event.dataObjectDeserializer.`object`.orElse(null) as? Subscription
                if (subscription != null) {
                    if (subscription.status.uppercase() == "ACTIVE") {
                        val localSubscription = getLocalSubscriptionFromStripeSubscription(subscription)
                            ?: return ResponseEntity.ok("Event received but local subscription was not found")
                        localSubscription.isActive = true
                        localSubscription.validThru = Timestamp(subscription.currentPeriodEnd)
                        subscriptionRepository.save(localSubscription)
                    }
                } else {
                    return ResponseEntity.ok("Event received but subscription object was null or of unexpected type")
                }
            }
        }

        return ResponseEntity.ok("Event received")
    }

    private fun checkIfSubscriptionCanBeCreated(userProfile: UserProfile, licencePlate: String) {
        userProfile.subscriptions.forEach { subscription ->
            if(subscription.licencePlate == licencePlate) throw BadRequestException("This user already has a subscription with this licence plate.")
        }
    }

    private fun getLocalSubscriptionFromStripeSubscription(subscription: Subscription): com.embeddedsystems.smartparkingbackend.entity.Subscription? {
        val licensePlate = subscription.metadata["license_plate"]
        val userId = subscription.metadata["user_id"]

        if(licensePlate.isNullOrEmpty() || userId.isNullOrEmpty()) throw BadRequestException("User id or licence plate is null")

        val user = userProfileRepository.findById(userId.toLong()).orElseThrow { EntityNotFoundException("User Profile", userId.toLong()) }
        return user.subscriptions.find { it.stripeSubscriptionId == subscription.id }
    }
}