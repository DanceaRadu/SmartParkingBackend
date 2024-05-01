package com.embeddedsystems.smartparkingbackend.controller

import com.embeddedsystems.smartparkingbackend.dto.PaymentInfo
import com.embeddedsystems.smartparkingbackend.service.StripeService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/payment")
class PaymentController(private val stripeService: StripeService) {

    @PostMapping("/subscribe")
    fun createSubscriptionIntent(@RequestBody paymentInfo: PaymentInfo, authentication: Authentication) =
        stripeService.createSubscriptionIntent(paymentInfo, authentication.name)

    @PostMapping("/subscription/updates")
    fun handleSubscriptionUpdates(@RequestBody payload: String, request: HttpServletRequest): ResponseEntity<String> =
        stripeService.handleSubscriptionUpdates(payload, request)
}