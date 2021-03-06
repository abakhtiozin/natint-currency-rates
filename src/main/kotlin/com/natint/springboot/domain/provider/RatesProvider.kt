package com.natint.springboot.domain.provider

import com.natint.springboot.domain.rates.Rates
import java.time.LocalDate

interface RatesProvider {
    fun request(date: LocalDate): Rates
}