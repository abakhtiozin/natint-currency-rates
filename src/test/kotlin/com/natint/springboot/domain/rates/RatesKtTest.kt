package com.natint.springboot.domain.rates

import com.natint.springboot.domain.CurrencyCode.GBP
import com.natint.springboot.domain.CurrencyCode.USD
import com.natint.springboot.entity.CurrencyRateEntity
import com.natint.springboot.entity.RateDateEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

internal class RatesKtTest {

    @Test
    fun isEmpty() {
        val rates = Rates(LocalDate.now(), emptyList<Rate>())
        Assertions.assertTrue(rates.isEmpty())
    }

    @Test
    fun isNotEmpty() {
        val date = LocalDate.of(2017, 6, 5)
        val listOfRate = listOf(
                Rate(USD, 3.5),
                Rate(GBP, 5.5)
        )
        val rates = Rates(date, listOfRate)
        Assertions.assertTrue(rates.isNotEmpty())
    }

    @Test
    fun fromEntity() {
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

    @Test
    fun fromEmptyEntity() {
        val date = LocalDate.of(2017, 6, 5)
        val actual = Rates.from(date, emptyList())
        val expected = Rates(date, emptyList())
        Assertions.assertEquals(expected, actual)
    }
}