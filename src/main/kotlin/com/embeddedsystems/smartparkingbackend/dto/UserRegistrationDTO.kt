package com.embeddedsystems.smartparkingbackend.dto

data class UserRegistrationDTO(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)
