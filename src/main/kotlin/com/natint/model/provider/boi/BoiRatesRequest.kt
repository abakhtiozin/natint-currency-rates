package com.natint.model.provider.boi

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate

internal class BoiRatesRequest(private val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private var attempts = 3

    internal fun send(requestUrl: String): BoiRatesResponse {
        val responseEntity: ResponseEntity<String>? = try {
            restTemplate.getForEntity(requestUrl, String::class.java)
        } catch (e: ResourceAccessException) {
            logger.error("exception", e)
            null
        }
        val body = responseEntity?.body?.toLowerCase() ?: ""
        if (body.isEmpty() || hasErrors(body)) {
            attempts--
            while (attempts > 0) {
                return send(requestUrl)
            }
        }
        return BoiRatesResponse(body)
    }

    private fun hasErrors(rawResponse: String): Boolean {
        return !rawResponse.contains("currencies", true)
    }
}