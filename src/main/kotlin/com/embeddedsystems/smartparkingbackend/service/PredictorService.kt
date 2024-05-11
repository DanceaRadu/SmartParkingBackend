package com.embeddedsystems.smartparkingbackend.service

import com.embeddedsystems.smartparkingbackend.dto.PredictionRequestDTO
import com.embeddedsystems.smartparkingbackend.exceptions.types.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity

@Service
class PredictorService {

    @Value("\${predictor.url}")
    lateinit var predictorUrl: String
    var restTemplate = RestTemplate()

    fun predict(predictionRequest: PredictionRequestDTO): Map<String, Int> {
        predictionRequest.date ?: throw BadRequestException("Date is required for interval prediction")
        return mapProbabilityToParkingSpots(
            sendRequest("$predictorUrl/predict", predictionRequest)
        )
    }

    fun predictUsingInterval(predictionRequest: PredictionRequestDTO): Map<String, Int> {
        predictionRequest.start ?: throw BadRequestException("Start date is required for interval prediction")
        predictionRequest.end ?: throw BadRequestException("End date is required for interval prediction")
        return mapProbabilityToParkingSpots(
            sendRequest("$predictorUrl/predict/interval", predictionRequest)
        )
    }

    private fun sendRequest(url: String, predictionRequest: PredictionRequestDTO): Map<String, Double> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request: HttpEntity<PredictionRequestDTO> = HttpEntity(predictionRequest, headers)
        val responseType = object : ParameterizedTypeReference<Map<String, Double>>() {}

        try {
            val response: ResponseEntity<Map<String, Double>> =
                restTemplate.postForEntity(url, request, responseType)
            return response.body ?: throw BadRequestException("Error from Predictor: Empty response")
        } catch (e: HttpClientErrorException) {
            if (e.statusCode.value() == 400) {
                throw BadRequestException("Error from Predictor: ${e.responseBodyAsString}")
            } else {
                print(e.responseBodyAsString)
                throw e
            }
        }
    }

    private fun mapProbabilityToParkingSpots(probabilities: Map<String, Double>): Map<String, Int> {
        return probabilities.mapValues { (_, value) -> (value / 0.125).toInt() }
    }
}
