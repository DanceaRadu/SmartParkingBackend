package com.embeddedsystems.smartparkingbackend.controller

import com.embeddedsystems.smartparkingbackend.dto.ParkingConfigDTO
import com.embeddedsystems.smartparkingbackend.service.ParkingConfigService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/parking-config")
class ParkingConfigController(private val parkingConfigService: ParkingConfigService) {

    @GetMapping
    fun getLatestParkingConfig(): ParkingConfigDTO =
        parkingConfigService.getLatestParkingConfig()

    @PostMapping
    fun updateCurrentParkingConfig(@RequestBody parkingConfigDTO: ParkingConfigDTO) =
        parkingConfigService.updateCurrentParkingConfig(parkingConfigDTO)
}
