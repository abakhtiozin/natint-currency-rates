package com.natint.model.provider

import com.natint.model.Rates
import java.time.LocalDate

interface RatesProvider {
    fun request(date: LocalDate): Rates
}