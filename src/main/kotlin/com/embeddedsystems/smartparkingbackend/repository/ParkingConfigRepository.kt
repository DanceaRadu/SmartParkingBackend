package com.embeddedsystems.smartparkingbackend.repository

import com.embeddedsystems.smartparkingbackend.entity.ParkingConfig
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ParkingConfigRepository: JpaRepository<ParkingConfig, Long> {
    @Query("SELECT e FROM ParkingConfig e ORDER BY e.createdAt DESC")
    fun getConfigsOrderedByTime(): List<ParkingConfig>
}
