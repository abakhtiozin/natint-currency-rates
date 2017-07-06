package com.natint.model.provider

import com.natint.database.service.RateService
import com.natint.model.Rates
import java.time.LocalDate

open class DatabaseRates(
        private val ratesProvider: RatesProvider,
        private val rateService: RateService
) : RatesProvider {
    override fun request(date: LocalDate): Rates {
        val rates = ratesProvider.request(date)
        if (rates.isEmpty()) {
            val dbRates = rateService.findByDate(date)
            if (dbRates.isNotEmpty()) {
                return dbRates
            }
        } else {
            rateService.save(rates)
        }
        return rates
    }
}