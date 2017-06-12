package com.natint.model.provider

import com.beust.klaxon.JsonArray
import java.time.LocalDate


interface RatesProvider {
    fun request(date: LocalDate): JsonArray<Any?>
}