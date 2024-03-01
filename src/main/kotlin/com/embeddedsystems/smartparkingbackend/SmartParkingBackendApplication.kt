package com.embeddedsystems.smartparkingbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmartParkingBackendApplication

fun main(args: Array<String>) {
    runApplication<SmartParkingBackendApplication>(*args)
}
