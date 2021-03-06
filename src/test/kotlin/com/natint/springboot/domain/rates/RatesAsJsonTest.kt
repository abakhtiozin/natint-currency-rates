package com.natint.springboot.domain.rates

import com.natint.springboot.domain.CurrencyCode.EUR
import com.natint.springboot.domain.CurrencyCode.GBP
import com.natint.springboot.domain.CurrencyCode.USD
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class RatesAsJsonTest {
    @Test
    fun toJsonSingleElement() {
        val date = LocalDate.of(2017, 6, 5)
        val listOfRate = listOf(
                Rate(USD, 3.5)
        )
        val rates = Rates(date, listOfRate)
        val expected = "{\"date\":\"05-06-2017\",\"rates\":[{\"code\":\"USD\",\"rate\":\"3.5\"}]}"
        val representable = RatesAsJson(rates)
        val actual = representable.represent()
        Assertions.assertEquals(actual, expected)
    }

    @Test
    fun toJsonMultipleElements() {
        val date = LocalDate.of(2017, 6, 5)
        val listOfRate = listOf(
                Rate(USD, 3.5),
                Rate(GBP, 5.5),
                Rate(EUR, 4.5)
        )
        val rates = Rates(date, listOfRate)
        val expected = "{\"date\":\"05-06-2017\"," +
                "\"rates\":[" +
                "{\"code\":\"USD\",\"rate\":\"3.5\"}," +
                "{\"code\":\"GBP\",\"rate\":\"5.5\"}," +
                "{\"code\":\"EUR\",\"rate\":\"4.5\"}" +
                "]}"
        val representable = RatesAsJson(rates)
        val actual = representable.represent()
        Assertions.assertEquals(actual, expected)
    }

    @Test
    fun toJsonEmptyList() {
        val date = LocalDate.of(2017, 6, 5)
        val listOfRate = emptyList<Rate>()
        val rates = Rates(date, listOfRate)
        val expected = "{\"date\":\"05-06-2017\",\"rates\":[]}"
        val representable = RatesAsJson(rates)
        val actual = representable.represent()
        Assertions.assertEquals(actual, expected)
    }
}