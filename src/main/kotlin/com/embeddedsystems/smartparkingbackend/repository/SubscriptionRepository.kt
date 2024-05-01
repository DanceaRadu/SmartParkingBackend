package com.embeddedsystems.smartparkingbackend.repository

import com.embeddedsystems.smartparkingbackend.entity.Subscription
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubscriptionRepository: JpaRepository<Subscription, Long> {
    fun getSubscriptionByUserProfileId(userId: Long): Optional<Subscription>
    fun getSubscriptionByLicencePlate(licencePlate: String): Optional<Subscription>
}
