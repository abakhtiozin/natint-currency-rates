package com.natint.model.provider.boi

import com.natint.model.Rates
import com.natint.model.provider.RatesProvider
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

@Component
open class BoiRates(private val restTemplate: RestTemplate, private val url: String) : RatesProvider {
    override fun request(date: LocalDate): Rates {
        val request = BoiRatesRequest(restTemplate, date, url)
        val response = request.send()
        val rates = response.parse()
        return rates
    }
}