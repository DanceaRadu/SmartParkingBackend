package com.embeddedsystems.smartparkingbackend.service

import com.embeddedsystems.smartparkingbackend.dto.ParkingConfigDTO
import com.embeddedsystems.smartparkingbackend.entity.ParkingConfig
import com.embeddedsystems.smartparkingbackend.repository.ParkingConfigRepository
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class ParkingConfigService(
    private val parkingConfigRepository: ParkingConfigRepository,
    private val messagingTemplate: SimpMessagingTemplate
) {

    fun getLatestParkingConfig(): ParkingConfigDTO {
        val configList = parkingConfigRepository.getConfigsOrderedByTime()
        if (configList.isEmpty()) {
            return ParkingConfigDTO(
                null,
                Timestamp(System.currentTimeMillis()).toInstant(),
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false
            )
        }

        return ParkingConfigDTO(configList[0])
    }

    fun updateCurrentParkingConfig(parkingConfigDTO: ParkingConfigDTO) {
        val updatedConfig = parkingConfigRepository.save(
            ParkingConfig(parkingConfigDTO)
        )
        messagingTemplate.convertAndSend("/topic/parking-config", ParkingConfigDTO(updatedConfig))
    }
}
