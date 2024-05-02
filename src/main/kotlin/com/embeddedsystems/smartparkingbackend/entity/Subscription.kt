package com.embeddedsystems.smartparkingbackend.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.sql.Timestamp

@Entity
data class Subscription(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    @JsonManagedReference
    var userProfile: UserProfile,

    var licencePlate: String,

    var isActive: Boolean = false,

    var stripePriceId: String,

    @Column(unique = true)
    var stripeSubscriptionId: String,

    var validThru: Timestamp? = null
)
