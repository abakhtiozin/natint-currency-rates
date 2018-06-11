package com.natint.springboot.domain.rates

import com.natint.springboot.entity.CurrencyRateEntity
import java.time.LocalDate

data class Rates(val date: LocalDate, val values: List<Rate> = emptyList()) {
    fun isEmpty() = this.values.isEmpty()
    fun isNotEmpty() = this.values.isNotEmpty()

    companion object {
        fun from(date: LocalDate, currencyRateEntities: List<CurrencyRateEntity>): Rates {
            return Rates(date, currencyRateEntities.map {
                val currencyEntity = it.currencyEntity
                if (currencyEntity != null) {
                    val code = currencyEntity.code
                    val number = currencyEntity.number
                    if (code != null && number != null) {
                        Rate(code, it.rate, number)
                    }
                }
                Rate("", 0.0, 0)
            })
        }
    }
}

data class Rate(val code: String, val rate: Double, val number: Int)
