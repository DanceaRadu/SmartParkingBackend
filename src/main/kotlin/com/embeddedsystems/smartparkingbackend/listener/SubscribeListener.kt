package com.embeddedsystems.smartparkingbackend.listener

import com.embeddedsystems.smartparkingbackend.service.ParkingConfigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionSubscribeEvent


@Component
class SubscribeListener : ApplicationListener<SessionSubscribeEvent> {
    private var messagingTemplate: SimpMessagingTemplate? = null

    @Autowired
    private val parkingConfigService: ParkingConfigService? = null
    @Autowired
    fun subscribeListener(messagingTemplate: SimpMessagingTemplate?) {
        this.messagingTemplate = messagingTemplate
    }

    override fun onApplicationEvent(event: SessionSubscribeEvent) {
        parkingConfigService?.getLatestParkingConfig()
            ?.let { messagingTemplate!!.convertAndSend("/topic/parking-config", it) }
    }
}
