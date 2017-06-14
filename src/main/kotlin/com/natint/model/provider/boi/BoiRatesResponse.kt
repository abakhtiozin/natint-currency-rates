package com.natint.model.provider.boi

import com.natint.model.CurrencyCode.*
import com.natint.model.Rate
import com.natint.model.Rates
import com.natint.model.provider.boi.xml.mapping.Currencies
import com.natint.model.provider.boi.xml.mapping.Currency
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException

internal class BoiRatesResponse(private val date: LocalDate, private val body: String) {

    internal fun parse(): Rates {
        if (body.isNotBlank()) {
            val currencies = XmlBody(body).unmarshal()
            if (currencies.currencies != null && currencies.last_update != null) {
                return XmlRates(currencies).toRates()
            }
        }
        return Rates(date, arrayListOf<Rate>())
    }
}

private class XmlBody(private val body: String) {
    internal fun unmarshal(): Currencies = try {
        val xmlBody = body.substring(body.indexOf("<"))
        val inputStream = IOUtils.toInputStream(xmlBody, "UTF-8")
        val jaxbContext = JAXBContext.newInstance(Currencies::class.java)
        val jaxbUnmarshaller = jaxbContext.createUnmarshaller()
        jaxbUnmarshaller.unmarshal(inputStream) as Currencies
    } catch (ex: Exception) {
        val currencies = Currencies()
        when (ex) {
            is IOException, is JAXBException, is ParseException -> {
                ex.printStackTrace()
                currencies
            }
            else -> currencies
        }
    }
}

private class XmlRates(private val currencies: Currencies) {
    internal fun toRates(): Rates {
        val xmlCurrencies = currencies.currencies
        val ratesList = xmlCurrencies
                .filterNotNull()
                .filter { !it.currencycode.isNullOrBlank() && !it.rate.isNullOrBlank() }
                .filter { byRates(it) }
                .map {
                    val code = it.currencycode.toUpperCase()
                    val rate = it.rate.toDouble()
                    Rate(valueOf(code), rate)
                }

        val rateDate = getRateDate(currencies.last_update)
        return Rates(rateDate, ratesList)
    }

    private fun byRates(it: Currency) = valueOf(it.currencycode.toUpperCase()) in listOf(USD, EUR, GBP)

    private fun getRateDate(last_update: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(last_update, formatter)
    }
}
