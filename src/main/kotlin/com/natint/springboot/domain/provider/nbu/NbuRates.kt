package com.natint.springboot.domain.provider.nbu

import com.natint.springboot.domain.provider.RatesProvider
import com.natint.springboot.domain.rates.Rates
import com.natint.springboot.entity.ProviderEntity
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

class NbuRates(
        private val restTemplate: RestTemplate,
        private val providerEntity: ProviderEntity
) : RatesProvider {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun request(date: LocalDate): Rates {
        val urlEntity = providerEntity.urlEntity
        val url = urlEntity?.url
        val ratesAdapter = NbuRatesAdapter(NbuApi(restTemplate, url!!))
        val rates = ratesAdapter.request(date)
        logger.info("Rates for date $date: $rates")
        return rates
    }
}