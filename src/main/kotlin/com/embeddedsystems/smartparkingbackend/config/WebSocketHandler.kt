package com.embeddedsystems.smartparkingbackend.config

import com.embeddedsystems.smartparkingbackend.dto.ParkingConfigDTO
import com.embeddedsystems.smartparkingbackend.repository.ParkingConfigRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.sql.Timestamp


@Component
class WebSocketHandler(
    private val parkingConfigRepository: ParkingConfigRepository
) : TextWebSocketHandler() {

    private val sessions: MutableList<WebSocketSession> = mutableListOf<WebSocketSession>()
    private final var mapper: ObjectMapper = ObjectMapper()
    init {
        mapper.registerModule(JavaTimeModule())
    }

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
        session.sendMessage(
            TextMessage(
                mapper.writeValueAsString(getLatestConfig())
            )
        )
    }

    @Throws(Exception::class)
    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        println("Received message: ${message.payload}")
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session)
        println("WebSocket connection closed")
    }

    @Throws(Exception::class)
    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        System.err.println("Error in WebSocket communication: ${exception.message}")
    }

    fun sendUpdatedConfigToAll() {
        sessions.forEach { session ->
            if (session.isOpen) {
                try {
                    session.sendMessage(
                        TextMessage(mapper.writeValueAsString(getLatestConfig()))
                    )
                } catch (e: Exception) {
                    System.err.println("Failed to send message to ${session.id}: ${e.message}")
                }
            }
        }
    }

    private fun getLatestConfig(): ParkingConfigDTO {
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
}
