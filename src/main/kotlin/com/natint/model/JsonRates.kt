package com.natint.model

import com.beust.klaxon.JsonArray
import com.beust.klaxon.json
import java.time.format.DateTimeFormatter

class JsonRates(private val rates: Rates) {
    fun get(): JsonArray<Any?> {
        val pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = rates.date
        val json: JsonArray<Any?> = json {
            array(
                    obj(
                            "date" to pattern.format(date)
                    ),
                    obj(
                            "rates" to array(rates.rates.map {
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