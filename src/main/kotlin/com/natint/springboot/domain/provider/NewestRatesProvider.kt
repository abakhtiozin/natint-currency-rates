package com.natint.springboot.domain.provider

import com.natint.springboot.domain.rates.Rates
import org.slf4j.LoggerFactory
import java.time.LocalDate

class NewestRatesProvider(private val ratesProvider: RatesProvider) : RatesProvider {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private var dayCounter: Long = 0
    override fun request(date: LocalDate): Rates {
        val rates = ratesProvider.request(date)
        if (rates.values.isEmpty()) {
            logger.info("response return no values for date $date")
            while (dayCounter < 7) {
                dayCounter++
                return this.request(date.minusDays(1))
            }
        } else return Rates(date, rates.values)
        return Rates(date, emptyList())
    }
}
