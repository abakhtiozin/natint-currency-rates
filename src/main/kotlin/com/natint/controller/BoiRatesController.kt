package com.natint.controller

import com.beust.klaxon.JsonArray
import com.natint.database.entity.RequestType
import com.natint.model.json
import com.natint.model.listeners.RequestListener
import com.natint.model.provider.NewestRates
import com.natint.model.provider.RatesProvider
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/boi")
class BoiRatesController(val ratesProvider: RatesProvider, val requestListener: RequestListener) {

    @RequestMapping("/getNewestRates", method = arrayOf(GET), produces = arrayOf("application/json"))
    @ResponseBody fun getNewestRates(): JsonArray<Any?> {
        requestListener.notify(RequestType.NEWEST)
        val newestRates = NewestRates(ratesProvider)
        val rates = newestRates.request(LocalDate.now())
        return rates.json()
    }

    @RequestMapping("/getYesterdayRates", method = arrayOf(GET), produces = arrayOf("application/json"))
    @ResponseBody fun getYesterdayRates(): JsonArray<Any?> {
        requestListener.notify(RequestType.YESTERDAY)
        return ratesProvider.request(LocalDate.now().minusDays(1)).json()
    }

    @RequestMapping("/getRates/{requestDate}", method = arrayOf(GET), produces = arrayOf("application/json"))
    @ResponseBody fun getRates(@PathVariable requestDate: String): JsonArray<Any?> {
        requestListener.notify(RequestType.BY_DATE)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(requestDate, formatter)
        val rates = ratesProvider.request(date)
        return rates.json()
    }
}
