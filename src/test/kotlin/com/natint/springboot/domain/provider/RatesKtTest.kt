package com.natint.springboot.domain.provider

import com.natint.springboot.domain.CurrencyCode.*
import com.natint.springboot.domain.rates.Rate
import com.natint.springboot.domain.rates.Rates
import com.natint.springboot.domain.rates.RatesAsJson
import com.natint.springboot.entity.CurrencyRateEntity
import com.natint.springboot.entity.RateDateEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

internal class RatesKtTest {

    @Test fun toJsonSingleElement() {
        val date = LocalDate.of(2017, 6, 5)
        val listOfRate = listOf(
                Rate(USD, 3.5)
        )
        val rates = Rates(date, listOfRate)
        val expected = "[{\"date\":\"05-06-2017\"},{\"rates\":[{\"code\":\"USD\",\"rate\":\"3.5\"}]}]"
        val actual = RatesAsJson(rates).represent().toJsonString()
        Assertions.assertEquals(actual, expected)
    }

    @Test fun toJsonMultipleElements() {
        val date = LocalDate.of(2017, 6, 5)
        val listOfRate = listOf(
                Rate(USD, 3.5),
                Rate(GBP, 5.5),
                Rate(EUR, 4.5)
        )
        val rates = Rates(date, listOfRate)
        val expected = "[{\"date\":\"05-06-2017\"}," +
                "{\"rates\":" +
                "[{\"code\":\"USD\",\"rate\":\"3.5\"}," +
                "{\"code\":\"GBP\",\"rate\":\"5.5\"}," +
                "{\"code\":\"EUR\",\"rate\":\"4.5\"}]}]"
        val actual = RatesAsJson(rates).represent().toJsonString()
        Assertions.assertEquals(actual, expected)
    }

    @Test fun toJsonEmptyList() {
        val date = LocalDate.of(2017, 6, 5)
        val listOfRate = emptyList<Rate>()
        val rates = Rates(date, listOfRate)
        val expected = "[{\"date\":\"05-06-2017\"},{\"rates\":[]}]"
        val actual = RatesAsJson(rates).represent().toJsonString()
        Assertions.assertEquals(actual, expected)
    }

    @Test fun isEmpty() {
        val rates = Rates(LocalDate.now(), emptyList<Rate>())
        Assertions.assertTrue(rates.isEmpty())
    }

    @Test fun isNotEmpty() {
        val date = LocalDate.of(2017, 6, 5)
        val listOfRate = listOf(
                Rate(USD, 3.5),
                Rate(GBP, 5.5)
        )
        val rates = Rates(date, listOfRate)
        Assertions.assertTrue(rates.isNotEmpty())
    }

    @Test fun fromEntity() {
        val date = LocalDateTime.of(2017, 6, 5, 0, 0)
        val rateDateEntity = RateDateEntity(1, Timestamp.valueOf(date))
        val localDate = date.toLocalDate()
        val actual = Rates.from(localDate,
                listOf(
                        CurrencyRateEntity(1,
                                USD,
                                5.0,
                                rateDateEntity
                        ),
                        CurrencyRateEntity(2,
                                GBP,
                                4.0,
                                rateDateEntity
                        )
                ))
        val expected = Rates(localDate, listOf(
                Rate(USD, 5.0),
                Rate(GBP, 4.0)
        ))
        Assertions.assertEquals(expected, actual)
    }

    @Test fun fromEmptyEntity() {
        val date = LocalDate.of(2017, 6, 5)
        val actual = Rates.from(date, emptyList())
        val expected = Rates(date, emptyList())
        Assertions.assertEquals(expected, actual)
    }
}