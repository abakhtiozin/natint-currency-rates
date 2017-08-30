package com.natint.springboot.domain.provider.boi

import com.natint.springboot.domain.provider.RatesProvider
import com.natint.springboot.domain.rates.Rates
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

class BoiRates(
        private val restTemplate: RestTemplate,
        private val url: String
) : RatesProvider {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun request(date: LocalDate): Rates {
        val request = BoiRatesRequest(restTemplate, date, url)
        val response = request.send()
        val rates = response.parse()
        logger.info("Rates for date $date: $rates")
        return rates
    }
}