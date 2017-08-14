package com.natint.springboot.domain

enum class DatePattern(val pattern: String) {
    Application("dd-MM-yyyy"),
    BoiResponse("yyyy-MM-dd"),
    BoiRequest("yyyyMMdd");

    val value: String get() = pattern
}