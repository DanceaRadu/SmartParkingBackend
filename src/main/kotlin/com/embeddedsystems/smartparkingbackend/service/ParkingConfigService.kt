package com.embeddedsystems.smartparkingbackend.service

import com.embeddedsystems.smartparkingbackend.config.WebSocketHandler
import com.embeddedsystems.smartparkingbackend.dto.ParkingConfigDTO
import com.embeddedsystems.smartparkingbackend.entity.ParkingConfig
import com.embeddedsystems.smartparkingbackend.repository.ParkingConfigRepository
import com.embeddedsystems.smartparkingbackend.repository.SubscriptionRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp


@Service
class ParkingConfigService(
    private val parkingConfigRepository: ParkingConfigRepository,
    private val webSocketHandler: WebSocketHandler,
    private val subscriptionRepository: SubscriptionRepository
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
        parkingConfigRepository.save(ParkingConfig(parkingConfigDTO))
        webSocketHandler.sendUpdatedConfigToAll()
    }

    fun getActiveLicencePlates() =
        subscriptionRepository.findAll().filter { it.isActive }
}
