package com.natint.springboot.web

import com.beust.klaxon.JsonArray
import com.natint.springboot.domain.json
import com.natint.springboot.domain.provider.NewestRatesProvider
import com.natint.springboot.domain.provider.RatesProvider
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/boi")
class BoiRatesController(val ratesProvider: RatesProvider) {

    @GetMapping("/getNewestRates", produces = arrayOf("application/json"))
    @ResponseBody fun getNewestRates(): JsonArray<Any?> {
        val rates = NewestRatesProvider(ratesProvider).request(LocalDate.now())
        return rates.json()
    }

    @GetMapping("/getYesterdayRates", produces = arrayOf("application/json"))
    @ResponseBody fun getYesterdayRates(): JsonArray<Any?> {
        return ratesProvider.request(LocalDate.now().minusDays(1)).json()
    }

    @GetMapping("/getRates/{requestDate}", produces = arrayOf("application/json"))
    @ResponseBody fun getRates(@PathVariable requestDate: String): JsonArray<Any?> {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(requestDate, formatter)
        val rates = ratesProvider.request(date)
        return rates.json()
    }
}
