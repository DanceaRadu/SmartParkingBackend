package com.embeddedsystems.smartparkingbackend.controller

import com.embeddedsystems.smartparkingbackend.dto.UserProfileDTO
import com.embeddedsystems.smartparkingbackend.dto.UserRegistrationDTO
import com.embeddedsystems.smartparkingbackend.service.UserProfileService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user-profile")
class UserProfileController(private val userProfileService: UserProfileService) {


        @GetMapping("/me")
        fun getMyUserProfile(authentication: Authentication): UserProfileDTO =
                userProfileService.getMyUserProfile(authentication)

        @GetMapping("/{userId}")
        fun getUserById(@PathVariable userId: Long): UserProfileDTO =
                userProfileService.getUserById(userId)


        @PostMapping("/register")
        fun registerNewUser(@RequestBody userRegistrationDTO: UserRegistrationDTO) =
                userProfileService.registerNewUser(userRegistrationDTO)

}
