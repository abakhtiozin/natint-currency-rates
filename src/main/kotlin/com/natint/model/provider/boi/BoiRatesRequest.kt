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

    internal fun send(): BoiRatesResponse {
        val requestUrl = prepareUrl()
        val responseEntity: ResponseEntity<String>? = try {
            restTemplate.getForEntity(requestUrl, String::class.java)
        } catch (e: ResourceAccessException) {
            logger.error("exception", e)
            null
        }
        val body = responseEntity?.body?.toLowerCase() ?: ""
        if (body.isEmpty() || body.hasErrors()) {
            attempts--
            Thread.sleep(3000)
            while (attempts > 0) {
                return send()
            }
        }
        return BoiRatesResponse(date, body)
    }

    private fun prepareUrl(): String {
        val pattern = "yyyyMMdd"
        val dateInString = date.format(DateTimeFormatter.ofPattern(pattern))
        return "$url$dateInString"
    }

    private fun String.hasErrors(): Boolean {
        return !this.contains("currencies", true)
    }
}