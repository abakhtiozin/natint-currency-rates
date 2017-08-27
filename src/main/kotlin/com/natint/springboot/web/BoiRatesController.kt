package com.natint.springboot.web

import com.natint.springboot.domain.DatePattern
import com.natint.springboot.domain.provider.NewestRatesProvider
import com.natint.springboot.domain.provider.RatesProvider
import com.natint.springboot.domain.rates.RatesAsJson
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/boi")
class BoiRatesController(private val ratesProvider: RatesProvider) {

    @GetMapping("/getNewestRates")
    @ResponseBody
    fun getNewestRates(): String {
        val provider = NewestRatesProvider(ratesProvider)
        val rates = provider.request(LocalDate.now())
        return RatesAsJson(rates).represent()
    }

    @GetMapping("/getYesterdayRates")
    @ResponseBody
    fun getYesterdayRates(): String {
        val yesterday = LocalDate.now().minusDays(1)
        val rates = ratesProvider.request(yesterday)
        return RatesAsJson(rates).represent()
    }

    @GetMapping("/getRates/{requestDate}")
    @ResponseBody
    fun getRates(@PathVariable requestDate: String): String {
        val pattern = DatePattern.Application
        val formatter = DateTimeFormatter.ofPattern(pattern.value)
        val date = LocalDate.parse(requestDate, formatter)
        val rates = ratesProvider.request(date)
        return RatesAsJson(rates).represent()
    }
}
