package com.embeddedsystems.smartparkingbackend.dto

import com.embeddedsystems.smartparkingbackend.entity.Subscription

class SubscriptionDTO(
    val id: Long?,
    val userProfileId: Long?,
    var licencePlate: String,
    var isActive: Boolean,
    var stripePriceId: String,
    var stripeSubscriptionId: String,
) {
    constructor(subscription: Subscription): this(
        id = subscription.id,
        userProfileId = subscription.userProfile.id,
        licencePlate = subscription.licencePlate,
        isActive = subscription.isActive,
        stripePriceId = subscription.stripePriceId,
        stripeSubscriptionId = subscription.stripeSubscriptionId
    )
}
