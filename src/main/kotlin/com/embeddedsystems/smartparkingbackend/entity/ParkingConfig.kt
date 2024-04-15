package com.embeddedsystems.smartparkingbackend.entity

import com.embeddedsystems.smartparkingbackend.dto.ParkingConfigDTO
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.sql.Timestamp

@Entity
data class ParkingConfig(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),

    val parkingSpot1: Boolean = false,
    val parkingSpot2: Boolean = false,
    val parkingSpot3: Boolean = false,
    val parkingSpot4: Boolean = false,
    val parkingSpot5: Boolean = false,
    val parkingSpot6: Boolean = false,
    val parkingSpot7: Boolean = false,
    val parkingSpot8: Boolean = false,
) {
    constructor(parkingConfigDTO: ParkingConfigDTO) : this(
        createdAt = Timestamp(System.currentTimeMillis()),
        parkingSpot1 = parkingConfigDTO.parkingSpot1,
        parkingSpot2 = parkingConfigDTO.parkingSpot2,
        parkingSpot3 = parkingConfigDTO.parkingSpot3,
        parkingSpot4 = parkingConfigDTO.parkingSpot4,
        parkingSpot5 = parkingConfigDTO.parkingSpot5,
        parkingSpot6 = parkingConfigDTO.parkingSpot6,
        parkingSpot7 = parkingConfigDTO.parkingSpot7,
        parkingSpot8 = parkingConfigDTO.parkingSpot8
    )
}
