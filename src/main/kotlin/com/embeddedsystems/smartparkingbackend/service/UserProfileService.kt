package com.embeddedsystems.smartparkingbackend.service

import com.embeddedsystems.smartparkingbackend.dto.UserProfileDTO
import com.embeddedsystems.smartparkingbackend.entity.UserProfile
import com.embeddedsystems.smartparkingbackend.exceptions.types.EntityNotFoundException
import com.embeddedsystems.smartparkingbackend.repository.UserProfileRepository
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository
) {

    fun create() {

    }

    fun getUserById(id: Long): UserProfileDTO =
        UserProfileDTO(
            userProfileRepository.findById(id).orElseThrow { EntityNotFoundException("UserProfile", id) }
        )


    fun getMyUserProfile(authentication: Authentication): UserProfileDTO  {
        val existingUserProfile = userProfileRepository.findByKeycloakId(authentication.name)
        return if (existingUserProfile != null) {
            UserProfileDTO(existingUserProfile)
        } else {
            val newUserProfile = UserProfile(
                keycloakId = authentication.name,
                username = getUserNameFromKeycloakAuthentication(authentication),
                email = getEmailFromKeycloakAuthentication(authentication)
            )
            UserProfileDTO(userProfileRepository.save(newUserProfile))
        }
    }


    fun delete() {

    }

    private fun getUserNameFromKeycloakAuthentication(authentication: Authentication): String {
        if (authentication is JwtAuthenticationToken) {
            return authentication.token.claims["preferred_username"] as String
        } else {
            throw IllegalArgumentException("Expected JwtAuthenticationToken but received ${authentication.javaClass}")
        }
    }

    private fun getEmailFromKeycloakAuthentication(authentication: Authentication): String {
        if (authentication is JwtAuthenticationToken) {
            return authentication.token.claims["email"] as String
        } else {
            throw IllegalArgumentException("Expected JwtAuthenticationToken but received ${authentication.javaClass}")
        }
    }
}
