package com.embeddedsystems.smartparkingbackend.entity

import jakarta.persistence.*

@Entity
data class UserProfile(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(unique = true, nullable = false)
    var keycloakId: String,

    var username: String,

    @OneToOne
    var subscription: Subscription? = null,
)
