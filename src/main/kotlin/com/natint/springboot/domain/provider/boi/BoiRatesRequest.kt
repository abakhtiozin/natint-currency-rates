package com.natint.springboot.domain.provider.boi

import com.natint.springboot.domain.DatePattern
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
        val pattern = DatePattern.BoiRequest
        val dateInString = date.format(DateTimeFormatter.ofPattern(pattern.value))
        "$url$dateInString"
    }

    internal fun send(): BoiRatesResponse {
        logger.info("Requesting rates from $urlWithParams")
        val responseEntity: ResponseEntity<String>? = try {
            restTemplate.getForEntity(urlWithParams, String::class.java)
        } catch (e: ResourceAccessException) {
            logger.error("exception", e)
            null
        }
        val responseBody = responseEntity?.body?.toLowerCase() ?: ""
        val body = Body(responseBody)
        if (body.isEmpty() || body.hasNoRates()) {
            logger.info("No dates returned from $url for date $date, attempt #$attempts")
            attempts--
            while (attempts > 0) {
                return send()
            }
        }
        return BoiRatesResponse(date, body)
    }
}