package com.embeddedsystems.smartparkingbackend.dto

import com.embeddedsystems.smartparkingbackend.entity.Subscription

class SubscriptionDTO(
    val id: Long?,
    val userProfileId: Long?
) {
    constructor(subscription: Subscription): this(
        id = subscription.id,
        userProfileId = subscription.userProfile.id
    )
}
