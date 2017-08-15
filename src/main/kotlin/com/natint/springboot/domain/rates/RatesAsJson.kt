package com.natint.springboot.domain.rates

import com.beust.klaxon.JsonArray
import com.natint.springboot.domain.DatePattern
import java.time.format.DateTimeFormatter

class RatesAsJson(private val rates: Rates) : Representable {
    override fun represent(): JsonArray<Any?> {
        val pattern = DatePattern.Application
        val formatter = DateTimeFormatter.ofPattern(pattern.value)
        val date = rates.date
        val json: JsonArray<Any?> = com.beust.klaxon.json {
            array(
                    obj(
                            "date" to formatter.format(date)
                    ),
                    obj(
                            "rates" to array(rates.values.map {
                                obj(
                                        "code" to it.currencyCode.toString(),
                                        "rate" to it.rate.toString()
                                )
                            })
                    )
            )
        }
        return json
    }
}