package com.embeddedsystems.smartparkingbackend.dto

import com.embeddedsystems.smartparkingbackend.entity.ParkingConfig
import java.time.Instant

data class ParkingConfigDTO(
    val id: Long?,
    val createdAt: Instant?,
    val parkingSpot1: Boolean,
    val parkingSpot2: Boolean,
    val parkingSpot3: Boolean,
    val parkingSpot4: Boolean,
    val parkingSpot5: Boolean,
    val parkingSpot6: Boolean,
    val parkingSpot7: Boolean,
    val parkingSpot8: Boolean
) {
    constructor(parkingConfig: ParkingConfig) : this(
        id = parkingConfig.id,
        createdAt = parkingConfig.createdAt.toInstant(),
        parkingSpot1 = parkingConfig.parkingSpot1,
        parkingSpot2 = parkingConfig.parkingSpot2,
        parkingSpot3 = parkingConfig.parkingSpot3,
        parkingSpot4 = parkingConfig.parkingSpot4,
        parkingSpot5 = parkingConfig.parkingSpot5,
        parkingSpot6 = parkingConfig.parkingSpot6,
        parkingSpot7 = parkingConfig.parkingSpot7,
        parkingSpot8 = parkingConfig.parkingSpot8
    )
}
