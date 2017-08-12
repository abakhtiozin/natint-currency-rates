package com.natint.springboot.domain

import com.beust.klaxon.JsonArray
import com.natint.springboot.entity.CurrencyRateEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Rates(val date: LocalDate, val rates: List<Rate>) {
    fun isEmpty() = this.rates.isEmpty()
    fun isNotEmpty() = this.rates.isNotEmpty()

    companion object {
        fun from(date: LocalDate, currencyRateEntities: List<CurrencyRateEntity>): Rates {
            val listOfRates = currencyRateEntities.map {
                Rate(it.currencyCode, it.rate)
            }
            return Rates(date, listOfRates)
        }
    }
}

data class Rate(val currencyCode: CurrencyCode, val rate: Double)

fun Rates.json(): JsonArray<Any?> {
    val pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val date = this.date
    val json: JsonArray<Any?> = com.beust.klaxon.json {
        array(
                obj(
                        "date" to pattern.format(date)
                ),
                obj(
                        "rates" to array(rates.map {
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