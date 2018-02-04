package com.natint.springboot.domain.provider

import com.natint.springboot.domain.CurrencyCode
import com.natint.springboot.domain.provider.boi.BoiRates
import com.natint.springboot.domain.rates.Rate
import com.natint.springboot.domain.rates.Rates
import com.natint.springboot.entity.UrlEntity
import com.natint.springboot.service.UrlService
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

internal class NewestRatesProviderTest {
    @Test
    fun request() {
        val url = "test/"
        val date = LocalDate.of(2017, 6, 5)
        val responseBody = "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
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
        val responseEntity = ResponseEntity(responseBody, HttpStatus.OK)
        val restTemplate = mock<RestTemplate> {
            on { getForEntity("${url}20170605", String::class.java) } doReturn responseEntity
        }
        val urlService = mock<UrlService> {
            on { get() } doReturn UrlEntity(url = url)
        }
        val boiRates = BoiRates(restTemplate, urlService)
        val newestRatesProvider = NewestRatesProvider(boiRates)
        val actual = newestRatesProvider.request(date)
        val expected = Rates(date,
                listOf(
                        Rate(CurrencyCode.USD, 3.518),
                        Rate(CurrencyCode.GBP, 4.486),
                        Rate(CurrencyCode.EUR, 3.9593)
                )
        )
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun requestMinusDay() {
        val url = "test/"

        val date = LocalDate.of(2017, 6, 6)
        val responseBody = "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n"
        val responseEntity = ResponseEntity(responseBody, HttpStatus.OK)

        val yesterdayResponseBody = "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
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
        val yesterdayResponseEntity = ResponseEntity(yesterdayResponseBody, HttpStatus.OK)
        val restTemplate = mock<RestTemplate> {
            on { getForEntity("${url}20170606", String::class.java) } doReturn responseEntity
            on { getForEntity("${url}20170605", String::class.java) } doReturn yesterdayResponseEntity
        }
        val urlService = mock<UrlService> {
            on { get() } doReturn UrlEntity(url = url)
        }
        val boiRates = BoiRates(restTemplate, urlService)
        val newestRatesProvider = NewestRatesProvider(boiRates)
        val actual = newestRatesProvider.request(date)
        val expected = Rates(LocalDate.of(2017, 6, 5),
                listOf(
                        Rate(CurrencyCode.USD, 3.518),
                        Rate(CurrencyCode.GBP, 4.486),
                        Rate(CurrencyCode.EUR, 3.9593)
                )
        )
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun emptyForPeriod() {
        val url = "test/"

        val date = LocalDate.of(2017, 6, 6)
        val responseBody = "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n"
        val responseEntity = ResponseEntity(responseBody, HttpStatus.OK)

        val restTemplate = mock<RestTemplate> {
            on { getForEntity("${url}20170606", String::class.java) } doReturn responseEntity
            on { getForEntity("${url}20170605", String::class.java) } doReturn responseEntity
            on { getForEntity("${url}20170604", String::class.java) } doReturn responseEntity
            on { getForEntity("${url}20170603", String::class.java) } doReturn responseEntity
            on { getForEntity("${url}20170602", String::class.java) } doReturn responseEntity
            on { getForEntity("${url}20170601", String::class.java) } doReturn responseEntity
            on { getForEntity("${url}20170531", String::class.java) } doReturn responseEntity
            on { getForEntity("${url}20170530", String::class.java) } doReturn responseEntity
        }
        val urlService = mock<UrlService> {
            on { get() } doReturn UrlEntity(url = url)
        }
        val boiRates = BoiRates(restTemplate, urlService)
        val newestRatesProvider = NewestRatesProvider(boiRates)
        val actual = newestRatesProvider.request(date)
        val expected = Rates(LocalDate.of(2017, 5, 30), emptyList())
        Assertions.assertEquals(expected, actual)
    }

}