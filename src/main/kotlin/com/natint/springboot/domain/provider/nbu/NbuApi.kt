package com.natint.springboot.domain.provider.nbu

import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NbuApi(private val restTemplate: RestTemplate, private val url: String) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    fun request(date: LocalDate): Array<CurrencyRateNBU> {
        val dateValue = date.format(dateTimeFormatter)
        val response = restTemplate.getForEntity("$url$dateValue", Array<CurrencyRateNBU>::class.java)
        return response.body
    }
}