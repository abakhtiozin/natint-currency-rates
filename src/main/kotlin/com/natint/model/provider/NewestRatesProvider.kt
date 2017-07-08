package com.natint.model.provider

import com.natint.model.Rates
import org.slf4j.LoggerFactory
import java.time.LocalDate

class NewestRatesProvider(private val ratesProvider: RatesProvider) : RatesProvider {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private var dayCounter: Long = 0
    override fun request(date: LocalDate): Rates {
        val rates = ratesProvider.request(date)
        if (rates.rates.isEmpty()) {
            logger.info("response return no rates for date $date")
            while (dayCounter < 7) {
                dayCounter++
                return this.request(date.minusDays(1))
            }
        } else return Rates(date, rates.rates)
        return Rates(date, emptyList())
    }
}
