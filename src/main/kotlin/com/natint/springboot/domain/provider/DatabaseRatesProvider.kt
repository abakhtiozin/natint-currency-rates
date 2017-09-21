package com.natint.springboot.domain.provider

import com.natint.springboot.domain.rates.Rates
import com.natint.springboot.service.RateService
import org.slf4j.LoggerFactory
import java.time.LocalDate

class DatabaseRatesProvider(
        private val ratesProvider: RatesProvider,
        private val rateService: RateService
) : RatesProvider {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun request(date: LocalDate): Rates {
        var rates = rateService.find(date)
        if (rates.isNotEmpty()) {
            logger.info("get rates from db")
        } else {
            logger.info("try to get from provider")
            rates = ratesProvider.request(date)
            if (rates.isNotEmpty()) {
                logger.info("save $rates to database")
                rateService.save(rates)
            }
        }
        return rates
    }
}