package com.natint.springboot.domain.provider.boi

import com.natint.springboot.domain.CurrencyCode
import com.natint.springboot.domain.rates.Rate
import com.natint.springboot.domain.rates.Rates
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class BoiRatesResponseTest {
    @Test
    fun blankBody() {
        val date = LocalDate.of(2017, 6, 5)
        val ratesResponse = BoiRatesResponse(date, Body(""))
        val actual = ratesResponse.parse()
        val expected = Rates(date, emptyList<Rate>())
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun incorrectBody() {
        val date = LocalDate.of(2017, 6, 5)
        val ratesResponse = BoiRatesResponse(date, Body("<?xml></xml>"))
        val actual = ratesResponse.parse()
        val expected = Rates(date, emptyList<Rate>())
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun noRateForSpecifiedDate() {
        val date = LocalDate.of(2017, 6, 5)
        val body = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                "<currencies>\n" +
                "  <requested_date>20170628</requested_date>\n" +
                "  <error1>requested date is invalid or</error1>\n" +
                "  <error2>no exchange rate published for this date</error2>\n" +
                "  <error3>attention: date should be in format yyyymmdd</error3>\n" +
                "</currencies>"
        val ratesResponse = BoiRatesResponse(date, Body(body))
        val actual = ratesResponse.parse()
        val expected = Rates(date, emptyList<Rate>())
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun validResponse() {
        val date = LocalDate.of(2017, 6, 5)
        val body = "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                "<CURRENCIES>\n" +
                "  <LAST_UPDATE>2017-06-05</LAST_UPDATE>\n" +
                "  <CURRENCY>\n" +
                "    <NAME>Dollar</NAME>\n" +
                "    <UNIT>1</UNIT>\n" +
                "    <CURRENCYCODE>USD</CURRENCYCODE>\n" +
                "    <COUNTRY>USA</COUNTRY>\n" +
                "    <RATE>3.518</RATE>\n" +
                "    <CHANGE>-0.509</CHANGE>\n" +
                "  </CURRENCY>\n" +
                "  <CURRENCY>\n" +
                "    <NAME>Pound</NAME>\n" +
                "    <UNIT>1</UNIT>\n" +
                "    <CURRENCYCODE>GBP</CURRENCYCODE>\n" +
                "    <COUNTRY>Great Britain</COUNTRY>\n" +
                "    <RATE>4.4860</RATE>\n" +
                "    <CHANGE>-0.298</CHANGE>\n" +
                "  </CURRENCY>\n" +
                "  <CURRENCY>\n" +
                "    <NAME>Yen</NAME>\n" +
                "    <UNIT>100</UNIT>\n" +
                "    <CURRENCYCODE>JPY</CURRENCYCODE>\n" +
                "    <COUNTRY>Japan</COUNTRY>\n" +
                "    <RATE>3.1485</RATE>\n" +
                "    <CHANGE>-0.556</CHANGE>\n" +
                "  </CURRENCY>\n" +
                "  <CURRENCY>\n" +
                "    <NAME>Euro</NAME>\n" +
                "    <UNIT>1</UNIT>\n" +
                "    <CURRENCYCODE>EUR</CURRENCYCODE>\n" +
                "    <COUNTRY>EMU</COUNTRY>\n" +
                "    <RATE>3.9593</RATE>\n" +
                "    <CHANGE>0.182</CHANGE>\n" +
                "  </CURRENCY>\n" +
                "</CURRENCIES>"
        val ratesResponse = BoiRatesResponse(date, Body(body.toLowerCase()))
        val actual = ratesResponse.parse()
        val expected = Rates(date, listOf(
                Rate(CurrencyCode.USD, 3.518),
                Rate(CurrencyCode.GBP, 4.486),
                Rate(CurrencyCode.EUR, 3.9593)
        )
        )
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun wrongDateFormatResponse() {
        val date = LocalDate.of(2017, 6, 5)
        val body = "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                "<CURRENCIES>\n" +
                "  <LAST_UPDATE>2017/06/05</LAST_UPDATE>\n" +
                "  <CURRENCY>\n" +
                "    <NAME>Dollar</NAME>\n" +
                "    <UNIT>1</UNIT>\n" +
                "    <CURRENCYCODE>USD</CURRENCYCODE>\n" +
                "    <COUNTRY>USA</COUNTRY>\n" +
                "    <RATE>3.518</RATE>\n" +
                "    <CHANGE>-0.509</CHANGE>\n" +
                "  </CURRENCY>\n" +
                "</CURRENCIES>"
        val ratesResponse = BoiRatesResponse(date, Body(body.toLowerCase()))
        val actual = ratesResponse.parse()
        val expected = Rates(date, emptyList<Rate>())
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun wrongDateXlmElementResponse() {
        val date = LocalDate.of(2017, 6, 5)
        val body = "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                "<CURRENCIES>\n" +
                "  <DATE_UPDATE>2017-06-06</DATE_UPDATE>\n" +
                "  <CURRENCY>\n" +
                "    <NAME>Dollar</NAME>\n" +
                "    <UNIT>1</UNIT>\n" +
                "    <CURRENCYCODE>USD</CURRENCYCODE>\n" +
                "    <COUNTRY>USA</COUNTRY>\n" +
                "    <RATE>3.518</RATE>\n" +
                "    <CHANGE>-0.509</CHANGE>\n" +
                "  </CURRENCY>\n" +
                "</CURRENCIES>"
        val ratesResponse = BoiRatesResponse(date, Body(body.toLowerCase()))
        val actual = ratesResponse.parse()
        val expected = Rates(date, emptyList<Rate>())
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun emptyCurrencyXlmElementResponse() {
        val date = LocalDate.of(2017, 6, 5)
        val body = "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                "<CURRENCIES>\n" +
                "  <LAST_UPDATE>2017-06-05</LAST_UPDATE>\n" +
                "</CURRENCIES>"
        val ratesResponse = BoiRatesResponse(date, Body(body.toLowerCase()))
        val actual = ratesResponse.parse()
        val expected = Rates(date, emptyList<Rate>())
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun notRequestedDate() {
        val date = LocalDate.of(2017, 6, 5)
        val body = "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                "<CURRENCIES>\n" +
                "  <LAST_UPDATE>2017-06-04</LAST_UPDATE>\n" +
                "  <CURRENCY>\n" +
                "    <NAME>Dollar</NAME>\n" +
                "    <UNIT>1</UNIT>\n" +
                "    <CURRENCYCODE>USD</CURRENCYCODE>\n" +
                "    <COUNTRY>USA</COUNTRY>\n" +
                "    <RATE>3.518</RATE>\n" +
                "    <CHANGE>-0.509</CHANGE>\n" +
                "  </CURRENCY>\n" +
                "</CURRENCIES>"
        val ratesResponse = BoiRatesResponse(date, Body(body.toLowerCase()))
        val actual = ratesResponse.parse()
        val expected = Rates(date, emptyList<Rate>())
        Assertions.assertEquals(expected, actual)
    }
}