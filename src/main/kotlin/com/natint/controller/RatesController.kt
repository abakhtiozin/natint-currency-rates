package com.natint.controller

import com.beust.klaxon.JsonArray
import org.springframework.web.bind.annotation.PathVariable

interface RatesController {
    fun getRates(@PathVariable requestDate: String): JsonArray<Any?>

    fun getYesterdayRates(): JsonArray<Any?>

    fun getNewestRates(): JsonArray<Any?>
}

