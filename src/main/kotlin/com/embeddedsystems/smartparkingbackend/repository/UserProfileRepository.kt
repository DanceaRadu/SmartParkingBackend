package com.embeddedsystems.smartparkingbackend.repository

import com.embeddedsystems.smartparkingbackend.entity.UserProfile
import org.springframework.data.jpa.repository.JpaRepository

interface UserProfileRepository: JpaRepository<UserProfile, Long> {
    fun findByKeycloakId(keycloakId: String): UserProfile?
}
