package com.embeddedsystems.smartparkingbackend.config

import com.stripe.Stripe
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class StripeConfig {

    @Value("\${stripe.secret-key}")
    lateinit var secretKey: String

    @PostConstruct
    fun setup() {
        Stripe.apiKey = secretKey
    }
}