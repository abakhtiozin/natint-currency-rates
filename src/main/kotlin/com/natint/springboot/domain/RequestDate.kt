package com.natint.springboot.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RequestDate(private val requestDate: String) {
    val date = run {
        val pattern = DatePattern.Application
        val formatter = DateTimeFormatter.ofPattern(pattern.value)
        LocalDate.parse(requestDate, formatter)
    }
}