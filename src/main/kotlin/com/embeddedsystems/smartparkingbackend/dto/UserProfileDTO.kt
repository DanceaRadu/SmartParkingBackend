package com.embeddedsystems.smartparkingbackend.dto

import com.embeddedsystems.smartparkingbackend.entity.UserProfile

class UserProfileDTO(
    val id: Long?,
    val keycloakId: String?,
    val username: String?,
    val email: String?,
    val stripeCustomerId: String,
    val subscription: SubscriptionDTO?,
) {

    constructor(userProfile: UserProfile): this(
        id = userProfile.id,
        keycloakId = userProfile.keycloakId,
        username = userProfile.username,
        email = userProfile.email,
        stripeCustomerId = userProfile.stripeCustomerId ?: "",
        subscription = userProfile.subscription?.let { SubscriptionDTO(it) }
    )
}
