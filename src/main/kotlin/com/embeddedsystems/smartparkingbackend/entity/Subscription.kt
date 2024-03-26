package com.embeddedsystems.smartparkingbackend.entity

import jakarta.persistence.*

@Entity
data class Subscription(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToOne(cascade = [CascadeType.ALL])
    var userProfile: UserProfile
)
