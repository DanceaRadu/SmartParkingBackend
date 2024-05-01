package com.embeddedsystems.smartparkingbackend.dto

import com.embeddedsystems.smartparkingbackend.entity.UserProfile

class UserProfileDTO(
    val id: Long?,
    val keycloakId: String?,
    val username: String?,
    val email: String?,
    val stripeCustomerId: String,
    val subscriptions: List<SubscriptionDTO> = listOf(),
) {

    constructor(userProfile: UserProfile): this(
        id = userProfile.id,
        keycloakId = userProfile.keycloakId,
        username = userProfile.username,
        email = userProfile.email,
        stripeCustomerId = userProfile.stripeCustomerId ?: "",
        subscriptions = userProfile.subscriptions.map { SubscriptionDTO(it) },
    )
}
