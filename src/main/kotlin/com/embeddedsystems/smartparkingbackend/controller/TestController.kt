package com.embeddedsystems.smartparkingbackend.controller

import com.embeddedsystems.smartparkingbackend.dto.TestResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/test")
class TestController {

    @GetMapping("insecure")
    fun getTestInsecure() = TestResponse("Hello from insecure")

    @GetMapping("secure")
    fun getTestSecure() = TestResponse("Hello from insecure")
}
