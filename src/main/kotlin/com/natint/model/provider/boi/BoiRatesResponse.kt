package com.natint.model.provider.boi

import com.natint.model.CurrencyCode
import com.natint.model.CurrencyCode.valueOf
import com.natint.model.Rate
import com.natint.model.Rates
import com.natint.model.provider.boi.xml.mapping.Currencies
import com.natint.model.provider.boi.xml.mapping.Currency
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.io.IOException
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException

internal class BoiRatesResponse(private val date: LocalDate, private val body: Body) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    internal fun parse(): Rates {
        if (body.isNotEmpty()) {
            val currencies = XmlBody(body.clean()).unmarshal()
            if (currencies.currencies != null && currencies.last_update != null) {
                return XmlRates(currencies).toRates()
            }
            logger.info(" Error in Currencies: " + currencies.currencies +
                    " or currency rate date " + currencies.last_update)
        }
        logger.info("Response Body: $body")
        return Rates(date, arrayListOf<Rate>())
    }

    private inner class XmlRates(private val currencies: Currencies) {
        internal fun toRates(): Rates {
            val xmlCurrencies = currencies.currencies
            val ratesList = xmlCurrencies
                    .asSequence()
                    .filter { (it.currencycode.isNullOrBlank() && it.rate.isNullOrBlank()).not() }
                    .filter { byRates(it) }
                    .map {
                        val code = it.currencycode.toUpperCase()
                        val rate = it.rate.toDouble()
                        Rate(valueOf(code), rate)
                    }
                    .toList()
            val rateDate = getRateDate(currencies.last_update)
            if (rateDate == null || !date.isEqual(rateDate)) {
                logger.info("INFO: Expected rate date is $date but get $rateDate in response")
                return Rates(date, emptyList())
            }
            return Rates(rateDate, ratesList)
        }

        private fun byRates(it: Currency): Boolean {
            return it.currencycode.toUpperCase() in CurrencyCode.values()
                    .map { it.toString() }
        }

        private fun getRateDate(last_update: String): LocalDate? {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return try {
                LocalDate.parse(last_update, formatter)
            } catch (ex: DateTimeParseException) {
                ex.printStackTrace()
                null
            }
        }
    }
}

private class XmlBody(private val xmlBody: Body) {
    private val encoding = "UTF-8"

    internal fun unmarshal(): Currencies = try {
        val inputStream = IOUtils.toInputStream(xmlBody.toString(), encoding)
        val jaxbContext = JAXBContext.newInstance(Currencies::class.java)
        val jaxbUnmarshaller = jaxbContext.createUnmarshaller()
        jaxbUnmarshaller.unmarshal(inputStream) as Currencies
    } catch (ex: Exception) {
        val currencies = Currencies()
        when (ex) {
            is IOException,
            is JAXBException,
            is ParseException -> {
                ex.printStackTrace()
                currencies
            }
            else -> currencies
        }
    }
}
