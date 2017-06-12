package com.natint.model.provider.boi

import com.beust.klaxon.JsonArray
import com.natint.model.JsonRates
import com.natint.model.provider.RatesProvider
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
open class BoiRates(private val restTemplate: RestTemplate, private val url: String) : RatesProvider {
    override fun request(date: LocalDate): JsonArray<Any?> {
        val request = BoiRatesRequest(restTemplate)
        val requestUrl = prepareUrl(date)
        val response = request.send(requestUrl)
        val rates = response.parse()
        return JsonRates(rates).get()
    }

    private fun prepareUrl(date: LocalDate): String {
        val pattern = "yyyyMMdd"
        val dateInString = date.format(DateTimeFormatter.ofPattern(pattern))
        return "$url$dateInString"
    }
}