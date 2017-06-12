package com.natint.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.format.DateTimeParseException

@ControllerAdvice
class ExceptionController : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = *arrayOf(DateTimeParseException::class))
    fun handleConflict(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, ex.toString(),
                HttpHeaders(), HttpStatus.CONFLICT, request)
    }
}