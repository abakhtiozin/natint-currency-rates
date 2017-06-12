package com.natint.model.provider.boi

import com.natint.model.CurrencyCode
import com.natint.model.CurrencyCode.valueOf
import com.natint.model.Rate
import com.natint.model.Rates
import com.natint.model.provider.boi.xml.mapping.Currencies
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException

internal class BoiRatesResponse(private val body: String) {

    internal fun parse(): Rates {
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
        return Rates(LocalDate.now(), arrayListOf<Rate>())
    }

    @Throws(ParseException::class)
    private fun fillCurrencyRateList(currencies: com.natint.model.provider.boi.xml.mapping.Currencies): Rates {
        val xmlCurrencies = currencies.currencies
        val ratesList = xmlCurrencies.map({
            val code = it.currencycode.toUpperCase()
            val rate = it.rate.toDouble()
            Rate(valueOf(code), rate)
        })
                .filter { byRates(it) }
        val rateDate = getRateDate(currencies.last_update)
        return Rates(rateDate, ratesList)
    }

    private fun byRates(it: Rate) = it.currencyCode in listOf(CurrencyCode.USD, CurrencyCode.EUR, CurrencyCode.GBP)

    private fun getRateDate(last_update: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(last_update, formatter)
    }
}


