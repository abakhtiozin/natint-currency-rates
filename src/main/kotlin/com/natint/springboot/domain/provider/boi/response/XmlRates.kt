package com.natint.springboot.domain.provider.boi.response

import com.natint.springboot.domain.CurrencyCode
import com.natint.springboot.domain.DatePattern
import com.natint.springboot.domain.provider.boi.xml.mapping.Currencies
import com.natint.springboot.domain.provider.boi.xml.mapping.Currency
import com.natint.springboot.domain.rates.Rate
import com.natint.springboot.domain.rates.Rates
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

internal class XmlRates(private val currencies: Currencies, private val date: LocalDate) {
    internal fun toRates(): Rates {
        val xmlCurrencies = currencies.getCurrencies()
        val ratesList = xmlCurrencies
                .filter { it.getCurrencycode().isNotBlank() && it.getRate().isNotBlank() }
                .filter { byRates(it) }
                .map {
                    val code = it.getCurrencycode().toUpperCase()
                    val rate = it.getRate().toDouble()
                    Rate(CurrencyCode.valueOf(code), rate)
                }
        val rateDate = getRateDate(currencies.getLast_update())
        if (rateDate == null || !date.isEqual(rateDate)) {
            return Rates(date, emptyList())
        }
        return Rates(rateDate, ratesList)
    }

    private fun byRates(it: Currency): Boolean {
        val codes = CurrencyCode.values()
                .map { it.toString() }
        return it.getCurrencycode().toUpperCase() in codes
    }

    private fun getRateDate(last_update: String): LocalDate? {
        val formatter = DateTimeFormatter.ofPattern(DatePattern.BoiResponse.value)
        return try {
            LocalDate.parse(last_update, formatter)
        } catch (ex: DateTimeParseException) {
            ex.printStackTrace()
            null
        }
    }
}