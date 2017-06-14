package com.natint.controller

import com.beust.klaxon.JsonArray
import com.natint.model.json
import com.natint.model.provider.NewestRates
import com.natint.model.provider.RatesProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/boi")
class BoiRatesController(@Autowired private val ratesProvider: RatesProvider) {

    @RequestMapping("/getNewestRates", method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    @ResponseBody fun getNewestRates(): JsonArray<Any?> {
        val newestRates = NewestRates(ratesProvider)
        val rates = newestRates.request(LocalDate.now())
        return rates.json()
    }

    @RequestMapping("/getYesterdayRates", method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    @ResponseBody fun getYesterdayRates() = ratesProvider.request(LocalDate.now().minusDays(1)).json()

    @RequestMapping("/getRates/{requestDate}", method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    @ResponseBody fun getRates(@PathVariable requestDate: String): JsonArray<Any?> {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(requestDate, formatter)
        val rates = ratesProvider.request(date)
        return rates.json()
    }
}
