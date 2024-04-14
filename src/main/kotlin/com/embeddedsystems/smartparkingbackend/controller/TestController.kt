package com.embeddedsystems.smartparkingbackend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/test")
class TestController {

    @GetMapping("insecure")
    fun getTestInsecure() = "Hello from insecure"

    @GetMapping("secure")
    fun getTestSecure() = "Hello from secure"
}
