package com.natint.springboot.domain.rates

import com.beust.klaxon.JsonObject
import com.beust.klaxon.json
import com.natint.springboot.domain.DatePattern
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RatesAsJson(private val rates: Rates) : Representable {
    override fun represent(): String {
        val pattern = DatePattern.Application
        val formatter = DateTimeFormatter.ofPattern(pattern.value)
        val date = rates.date
        val jsonArray = jsonObject(formatter, date)
        return jsonArray.toJsonString()
    }

    private fun jsonObject(formatter: DateTimeFormatter, date: LocalDate): JsonObject {
        return json {
            obj("date" to formatter.format(date),
                    "rates" to array(rates.values.map {
                        obj(
                                "code" to it.currencyCode.toString(),
                                "rate" to it.rate.toString()
                        )
                    })
            )
        }
    }
}