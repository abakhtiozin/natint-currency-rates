package com.natint.springboot.domain.provider.boi

import com.natint.springboot.domain.provider.boi.response.BoiRatesResponse
import com.natint.springboot.domain.provider.boi.response.XmlBody
import com.natint.springboot.domain.provider.boi.response.XmlRates
import com.natint.springboot.domain.rates.Rates
import org.slf4j.LoggerFactory

internal class BoiRatesAdapter(boiRatesResponse: BoiRatesResponse) {
    private val body = boiRatesResponse.body
    private val date = boiRatesResponse.date
    private val logger = LoggerFactory.getLogger(this.javaClass)

    internal fun getRates(): Rates {
        if (body.isNotEmpty()) {
            val currencies = XmlBody(body.clean()).unmarshal()
            if (currencies.getCurrencies().isNotEmpty()) {
                return XmlRates(currencies, date).toRates()
            }
            logger.info(" Error in Currencies: " + currencies.getCurrencies())
        }
        logger.info("Response ResponseBody: $body")
        return Rates(date, arrayListOf())
    }
}