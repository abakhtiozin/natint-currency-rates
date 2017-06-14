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
            try {
                val inputStream = IOUtils.toInputStream(body.substring(body.indexOf("<")), "UTF-8")
                val jaxbContext = JAXBContext.newInstance(Currencies::class.java)
                val jaxbUnmarshaller = jaxbContext.createUnmarshaller()
                val currencies = jaxbUnmarshaller.unmarshal(inputStream) as Currencies
                if (currencies.currencies != null && currencies.last_update != null) {
                    return fillCurrencyRateList(currencies)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JAXBException) {
                e.printStackTrace()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return Rates(date, arrayListOf<Rate>())
    }

    @Throws(ParseException::class)
    private fun fillCurrencyRateList(currencies: com.natint.model.provider.boi.xml.mapping.Currencies): Rates {
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
