package com.embeddedsystems.smartparkingbackend.exceptions

import com.embeddedsystems.smartparkingbackend.dto.ErrorResponse
import com.embeddedsystems.smartparkingbackend.exceptions.types.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class AppAdvice {

    @ResponseBody
    @ExceptionHandler(value = [
        EntityNotFoundException::class
    ])
    fun entityNotFoundHandler(ex: RuntimeException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "error", HttpStatus.NOT_FOUND)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ResponseBody
    @ExceptionHandler(value = [
        EntityAlreadyExistsException::class
    ])
    fun entityAlreadyExistsHandler(ex: EntityAlreadyExistsException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "error", HttpStatus.CONFLICT)
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ResponseBody
    @ExceptionHandler(value = [
        BadRequestException::class
    ])
    fun badRequestExceptionHandler(ex: RuntimeException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "error", HttpStatus.BAD_REQUEST)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ResponseBody
    @ExceptionHandler(value = [
        ConflictException::class
    ])
    fun conflictExceptionHandler(ex: RuntimeException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "error", HttpStatus.CONFLICT)
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ResponseBody
    @ExceptionHandler(value = [
        ForbiddenException::class
    ])
    fun forbiddenExceptionHandler(ex: RuntimeException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "error", HttpStatus.FORBIDDEN)
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }
}
