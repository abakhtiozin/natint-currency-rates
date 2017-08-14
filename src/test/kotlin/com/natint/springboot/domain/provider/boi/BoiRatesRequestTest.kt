package com.natint.springboot.domain.provider.boi

import com.natint.springboot.domain.CurrencyCode.*
import com.natint.springboot.domain.rates.Rate
import com.natint.springboot.domain.rates.Rates
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

class BoiRatesRequestTest {
    @Test
    fun `request rates valid`() {
        /*Given*/
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
        val boi = BoiRatesRequest(restTemplate, date, url)
        /*When*/
        val boiRatesResponse = boi.send()
        val actual = boiRatesResponse.parse()
        /*Then*/
        val expected = Rates(date,
                listOf(
                        Rate(USD, 3.518),
                        Rate(GBP, 4.486),
                        Rate(EUR, 3.9593)
                )
        )
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `request rates with exception thrown`() {
        /*Given*/
        val url = "test/"
        val date = LocalDate.of(2017, 6, 5)
        val restTemplate = mock<RestTemplate> {
            on { getForEntity("${url}20170605", String::class.java) } doThrow (ResourceAccessException(""))
        }
        val boi = BoiRatesRequest(restTemplate, date, url)
        /*When*/
        val boiRatesResponse = boi.send()
        val actual = boiRatesResponse.parse()
        /*Then*/
        val expected = Rates(date, emptyList())
        Assertions.assertEquals(expected, actual)
    }
}