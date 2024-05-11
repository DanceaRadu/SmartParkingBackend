package com.embeddedsystems.smartparkingbackend.controller

import com.embeddedsystems.smartparkingbackend.dto.PredictionRequestDTO
import com.embeddedsystems.smartparkingbackend.service.PredictorService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/predict")
class PredictorController(private val predictorService: PredictorService) {

    @PostMapping
    fun predict(@RequestBody predictionRequest: PredictionRequestDTO) =
        predictorService.predict(predictionRequest)

    @PostMapping("/interval")
    fun predictUsingInterval(@RequestBody predictionRequest: PredictionRequestDTO) =
        predictorService.predictUsingInterval(predictionRequest)
}
