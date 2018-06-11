package com.natint.springboot.domain.provider.nbu

import com.natint.springboot.domain.provider.RatesProvider
import com.natint.springboot.domain.rates.Rate
import com.natint.springboot.domain.rates.Rates
import com.natint.springboot.service.CurrencyService
import java.time.LocalDate

class NbuRatesAdapter(private val ratesProvider: NbuApi, private val currencyService: CurrencyService) : RatesProvider {

    override fun request(date: LocalDate): Rates {
        val response = ratesProvider.request(date)
        val providerCurrency = response.filter {
            val cc = it.cc
            val r030 = it.r030
            (cc != null && r030 != null)
        }
        val list = mapper(providerCurrency)
        if (providerCurrency.size > currencyService.count()) {
            currencyService.update {
                list
            }
        }
        return Rates(date, list)
    }

    private fun mapper(providerCurrency: List<CurrencyRateNBU>): List<Rate> {
        return providerCurrency.map {
            val code = it.cc
            val number = it.r030
            val rate = it.rate
            if (code != null && number != null && rate != null) {
                Rate(code, rate, number)
            } else Rate("", 0.0, 0)
        }
    }
}
