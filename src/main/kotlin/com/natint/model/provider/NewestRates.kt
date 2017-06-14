package com.natint.model.provider

import com.natint.model.Rates
import java.time.LocalDate

class NewestRates(private val ratesProvider: RatesProvider) : RatesProvider {
    private var dayCounter: Long = 0
    override fun request(date: LocalDate): Rates {
        val rates = ratesProvider.request(date)
        if (rates.rates.isEmpty()) {
            while (dayCounter < 7) {
                dayCounter++
                return this.request(date.minusDays(1))
            }
        } else return Rates(date, rates.rates)
        return Rates(date, emptyList())
    }
}

