package com.natint.controller

import com.beust.klaxon.JsonArray
import com.natint.model.provider.RatesProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/boi")
class BoiRatesController(@Autowired private val ratesProvider: RatesProvider) : RatesController {

    @RequestMapping("/getNewestRates", method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    @ResponseBody
    override fun getNewestRates() = ratesProvider.request(LocalDate.now())

    @RequestMapping("/getYesterdayRates", method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    @ResponseBody
    override fun getYesterdayRates() = ratesProvider.request(LocalDate.now().minusDays(1))

    @RequestMapping("/getRates/{requestDate}", method = arrayOf(RequestMethod.GET), produces = arrayOf("application/json"))
    @ResponseBody
    override fun getRates(@PathVariable requestDate: String): JsonArray<Any?> {
        val pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(requestDate, pattern)
        return ratesProvider.request(date)
    }
}
