package com.natint.springboot.web

import com.natint.springboot.domain.RequestDate
import com.natint.springboot.domain.provider.NewestRatesProvider
import com.natint.springboot.domain.provider.RatesProvider
import com.natint.springboot.domain.rates.RatesAsJson
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/boi/rates")
class BoiRatesController(private val ratesProvider: RatesProvider) {

    @GetMapping("/newest")
    @ResponseBody
    fun getNewestRates(): String {
        val provider = NewestRatesProvider(ratesProvider)
        val rates = provider.request(LocalDate.now())
        return RatesAsJson(rates).represent()
    }

    @GetMapping("/yesterday")
    @ResponseBody
    fun getYesterdayRates(): String {
        val yesterday = LocalDate.now().minusDays(1)
        val rates = ratesProvider.request(yesterday)
        return RatesAsJson(rates).represent()
    }

    @GetMapping("/{requestDate}")
    @ResponseBody
    fun getRates(@PathVariable requestDate: RequestDate): String {
        val rates = ratesProvider.request(requestDate.date)
        return RatesAsJson(rates).represent()
    }
}
