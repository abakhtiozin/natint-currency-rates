package com.natint.springboot.web

import com.natint.springboot.entity.UrlNotSetException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.format.DateTimeParseException

@ControllerAdvice("com.natint.springboot")
class ExceptionController : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = *arrayOf(DateTimeParseException::class))
    fun dateFormatConflict(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, "$ex. Correct date format is dd-MM-yyyy",
                HttpHeaders(), HttpStatus.CONFLICT, request)
    }

    @ExceptionHandler(value = *arrayOf(UrlNotSetException::class))
    fun urlNotSetConflict(ex: UrlNotSetException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, "currency provider url not set",
                HttpHeaders(), HttpStatus.CONFLICT, request)
    }
}