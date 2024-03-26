package com.embeddedsystems.smartparkingbackend.controller

import com.embeddedsystems.smartparkingbackend.service.UserProfileService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/user-profile")
class UserProfileController(private val userProfileService: UserProfileService) {

        @PostMapping
        fun create() {

        }

        @GetMapping
        fun getUserById() {

        }

        @GetMapping("/me")
        fun getMyUserProfile(authentication: Authentication) =
            userProfileService.getMyUserProfile(authentication.name)

        @DeleteMapping
        fun delete() {

        }
}
