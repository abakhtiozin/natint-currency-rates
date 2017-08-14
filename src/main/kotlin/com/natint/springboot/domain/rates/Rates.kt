package com.natint.springboot.domain.rates

import com.natint.springboot.domain.CurrencyCode
import com.natint.springboot.entity.CurrencyRateEntity
import java.time.LocalDate

data class Rates(val date: LocalDate, val rates: List<Rate>) {
    fun isEmpty() = this.rates.isEmpty()
    fun isNotEmpty() = this.rates.isNotEmpty()

    companion object {
        fun from(date: LocalDate, currencyRateEntities: List<CurrencyRateEntity>): Rates {
            return Rates(date, currencyRateEntities.map {
                Rate(it.currencyCode, it.rate)
            })
        }
    }
}

data class Rate(val currencyCode: CurrencyCode, val rate: Double)