package com.natint.springboot.domain.provider.boi

import com.natint.springboot.domain.provider.RatesProvider
import com.natint.springboot.domain.rates.Rates
import com.natint.springboot.service.UrlService
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

class BoiRates(
        private val restTemplate: RestTemplate,
        private val urlService: UrlService
) : RatesProvider {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun request(date: LocalDate): Rates {
        val url = urlService.get()
        val request = BoiRatesRequest(restTemplate, date, url)
        val response = request.send()
        val rates = BoiRatesAdapter(response).getRates()
        logger.info("Rates for date $date: $rates")
        return rates
    }
}