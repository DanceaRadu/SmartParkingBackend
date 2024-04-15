package com.embeddedsystems.smartparkingbackend.controller

import com.embeddedsystems.smartparkingbackend.dto.ParkingConfigDTO
import com.embeddedsystems.smartparkingbackend.service.ParkingConfigService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin

@Controller
class WebSocketController(private val parkingConfigService: ParkingConfigService) {
    @MessageMapping("/latest-config")
    @SendTo("/topic/parking-config")
    @CrossOrigin(origins = ["*"])
    fun sendLatestParkingConfig(): ParkingConfigDTO {
        return parkingConfigService.getLatestParkingConfig()
    }
}
