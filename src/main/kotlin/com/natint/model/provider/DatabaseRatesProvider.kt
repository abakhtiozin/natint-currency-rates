package com.natint.model.provider

import com.natint.database.service.RateService
import com.natint.model.Rates
import org.slf4j.LoggerFactory
import java.time.LocalDate

open class DatabaseRatesProvider(
        private val ratesProvider: RatesProvider,
        private val rateService: RateService
) : RatesProvider {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun request(date: LocalDate): Rates {
        val rates = ratesProvider.request(date)
        if (rates.isEmpty()) {
            logger.info("rates is empty")
            val dbRates = rateService.find(date)
            if (dbRates.isNotEmpty()) {
                logger.info("get rates from db")
                return dbRates
            }
        } else {
            logger.info("save $rates to database")
            rateService.save(rates)
        }
        return rates
    }
}