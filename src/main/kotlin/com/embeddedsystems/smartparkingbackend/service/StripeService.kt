package com.embeddedsystems.smartparkingbackend.service

import com.embeddedsystems.smartparkingbackend.dto.PaymentInfo
import com.embeddedsystems.smartparkingbackend.entity.UserProfile
import com.embeddedsystems.smartparkingbackend.exceptions.types.EntityNotFoundByNameException
import com.embeddedsystems.smartparkingbackend.repository.UserProfileRepository
import com.stripe.exception.StripeException
import com.stripe.model.Customer
import com.stripe.model.Event
import com.stripe.model.Subscription
import com.stripe.net.Webhook
import com.stripe.param.CustomerCreateParams
import com.stripe.param.CustomerUpdateParams
import com.stripe.param.SubscriptionCreateParams
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class StripeService(private val userProfileRepository: UserProfileRepository) {

    @Value("\${stripe.webhook-secret}")
    lateinit var endpointSecret: String

    fun createSubscriptionIntent(paymentInfo: PaymentInfo, keycloakId: String): Subscription {
        val userProfile = userProfileRepository.findByKeycloakId(keycloakId)
            ?: throw EntityNotFoundByNameException("User Profile", keycloakId)

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
            .putAllMetadata(metadata)
            .build()

        return Subscription.create(subscriptionParams)
    }

    fun attachPaymentMethodToCustomer(paymentMethodId: String, keycloakId: String) {
        val userProfile = userProfileRepository.findByKeycloakId(keycloakId)
            ?: throw EntityNotFoundByNameException("User Profile", keycloakId)

        val customer: Customer = Customer.retrieve(userProfile.stripeCustomerId)

        try {
            val params = CustomerUpdateParams.builder()
                .setInvoiceSettings(
                    CustomerUpdateParams.InvoiceSettings.builder()
                        .setDefaultPaymentMethod(paymentMethodId)
                        .build())
                .build()

            customer.update(params)
        } catch (e: StripeException) {
            e.printStackTrace()
        }
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

        val subscription = event.dataObjectDeserializer.`object`.orElse(null) as Subscription

        when (event.type) {
            "customer.subscription.created" -> {
                val licensePlate = subscription.metadata["license_plate"]
                val userId = subscription.metadata["user_id"]
                println("Created subscription for $userId, with the licence plate $licensePlate")
            }
            "customer.subscription.deleted" -> {
                val licensePlate = subscription.metadata["license_plate"]
                val userId = subscription.metadata["user_id"]
                println("Deleted subscription for $userId, with the licence plate $licensePlate")
            }
            "customer.subscription.updated" -> {
                val licensePlate = subscription.metadata["license_plate"]
                val userId = subscription.metadata["user_id"]
                println("Updated subscription for $userId, with the licence plate $licensePlate")
            }
        }

        return ResponseEntity.ok("Event received")
    }
}