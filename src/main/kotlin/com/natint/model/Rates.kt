package com.natint.model

import java.time.LocalDate

data class Rates(val date: LocalDate, val rates: List<Rate>)

data class Rate(val currencyCode: CurrencyCode, val rate: Double)