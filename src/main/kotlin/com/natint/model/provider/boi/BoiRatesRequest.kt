package com.natint.model.provider.boi

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class BoiRatesRequest(
        private val restTemplate: RestTemplate,
        private val date: LocalDate,
        private val url: String
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private var attempts = 3

    private val urlWithParams = run {
        val pattern = "yyyyMMdd"
        val dateInString = date.format(DateTimeFormatter.ofPattern(pattern))
        "$url$dateInString"
    }

    internal fun send(): BoiRatesResponse {
        val responseEntity: ResponseEntity<String>? = try {
            restTemplate.getForEntity(urlWithParams, String::class.java)
        } catch (e: ResourceAccessException) {
            logger.error("exception", e)
            Thread.sleep(3000) //boi site tends to return error from time to time
            null
        }
        val body = responseEntity?.body?.toLowerCase() ?: ""
        if (body.isEmpty() || body.hasNoRates()) {
            attempts--
            while (attempts > 0) {
                return send()
            }
        }
        return BoiRatesResponse(date, body)
    }

    private fun String.hasNoRates(): Boolean {
        return !this.contains("currencies", true)
    }
}