package com.embeddedsystems.smartparkingbackend.service

import com.embeddedsystems.smartparkingbackend.repository.UserProfileRepository
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository
) {

    fun create() {

    }

    fun getUserById() {

    }

    fun getMyUserProfile(keycloakId: String) =
        userProfileRepository.findByKeycloakId(keycloakId) ?:
        throw NoSuchElementException("UserProfile not found with keycloakId: $keycloakId")

    fun delete() {

    }
}
