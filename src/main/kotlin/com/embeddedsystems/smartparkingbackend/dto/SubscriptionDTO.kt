package com.embeddedsystems.smartparkingbackend.dto

import com.embeddedsystems.smartparkingbackend.entity.Subscription
import java.time.Instant

class SubscriptionDTO(
    val id: Long?,
    val userProfileId: Long?,
    var licencePlate: String,
    var isActive: Boolean,
    var stripePriceId: String,
    var stripeSubscriptionId: String,
    var validThru: Instant? = null
) {
    constructor(subscription: Subscription): this(
        id = subscription.id,
        userProfileId = subscription.userProfile.id,
        licencePlate = subscription.licencePlate,
        isActive = subscription.isActive,
        stripePriceId = subscription.stripePriceId,
        stripeSubscriptionId = subscription.stripeSubscriptionId,
        validThru = subscription.validThru?.toInstant()
    )
}
