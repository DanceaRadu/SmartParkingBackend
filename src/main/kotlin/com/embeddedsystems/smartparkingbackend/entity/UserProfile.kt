package com.embeddedsystems.smartparkingbackend.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
data class UserProfile(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,

    @Column(unique = true, nullable = false)
    var keycloakId: String,

    var username: String,

    var email: String,

    @Column(nullable = false)
    var stripeCustomerId: String? = null,

    @OneToMany(mappedBy = "userProfile", cascade = [(CascadeType.ALL)])
    @JsonBackReference
    var subscriptions: List<Subscription> = listOf(),
)
