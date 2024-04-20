package com.embeddedsystems.smartparkingbackend.service

import com.embeddedsystems.smartparkingbackend.dto.UserProfileDTO
import com.embeddedsystems.smartparkingbackend.dto.UserRegistrationDTO
import com.embeddedsystems.smartparkingbackend.entity.UserProfile
import com.embeddedsystems.smartparkingbackend.exceptions.types.BadRequestException
import com.embeddedsystems.smartparkingbackend.exceptions.types.EntityNotFoundException
import com.embeddedsystems.smartparkingbackend.repository.UserProfileRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository
) {

    @Value("\${keycloak.username}")
    private lateinit var keycloakUsername: String

    @Value("\${keycloak.password}")
    private lateinit var keycloakPassword: String

    @Value("\${keycloak.url}")
    private lateinit var keycloakUrl: String

    fun registerNewUser(userRegistrationDTO: UserRegistrationDTO) {

        checkIfUsernameExists(userRegistrationDTO.username)

        val accessToken = getKeycloakAdminToken()
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()

        headers.add("Authorization", "Bearer $accessToken")
        headers.contentType = MediaType.APPLICATION_JSON

        val newUser = """
        {
            "username": "${userRegistrationDTO.username}",
            "enabled": true,
            "emailVerified": true,
            "firstName": "${userRegistrationDTO.firstName}",
            "lastName": "${userRegistrationDTO.lastName}",
            "email": "${userRegistrationDTO.email}",
            "credentials": [
                {
                    "type": "password",
                    "value": "${userRegistrationDTO.password}",
                    "temporary": false
                }
            ]
        }
        """

        try {
            val userEntity = HttpEntity<String>(newUser, headers)
            restTemplate.postForEntity("${keycloakUrl}/admin/realms/parkingSI/users", userEntity, String::class.java)
        } catch (e: Exception) {
            if(e.message == null) {
                throw BadRequestException("Failed to create user.")
            }
            if(e.message!!.contains("409")) {
                throw BadRequestException("User with this email or username already exists.")
            }
            throw BadRequestException("Failed to create user in Keycloak")
        }
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

    private fun getKeycloakAdminToken(): String {

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val body = "client_id=admin-cli&username=${keycloakUsername}&password=${keycloakPassword}&grant_type=password"
        val entity = HttpEntity<String>(body, headers)
        val response = restTemplate.postForEntity("${keycloakUrl}/realms/master/protocol/openid-connect/token", entity, String::class.java)

        if (response.statusCode.is2xxSuccessful) {
            val mapper = ObjectMapper()
            mapper.registerKotlinModule()

            val node = mapper.readTree(response.body)
            return node.get("access_token")?.asText() ?: throw RuntimeException("Failed to get Keycloak admin token")

        } else {
            throw RuntimeException("Failed to get Keycloak admin token")
        }
    }

    private fun checkIfUsernameExists(username: String) {
        if (userProfileRepository.existsByUsername(username)) {
            throw BadRequestException("Username $username already exists")
        }
    }
}
