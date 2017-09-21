package com.natint.springboot.domain.provider.boi

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class BodyTest {
    val response =
            "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                    "<currencies>\n" +
                    "  <last_update>2017-08-14</last_update>\n" +
                    "  <currency>\n" +
                    "    <name>dollar</name>\n" +
                    "    <unit>1</unit>\n" +
                    "    <currencycode>usd</currencycode>\n" +
                    "    <country>usa</country>\n" +
                    "    <rate>3.583</rate>\n" +
                    "    <change>-0.084</change>\n" +
                    "  </currency>\n" +
                    "</currencies>"

    @Test
    fun isEmpty() {
        Assertions.assertTrue(ResponseBody("").isEmpty())
    }

    @Test
    fun isNotEmpty() {
        Assertions.assertTrue(ResponseBody(response).isNotEmpty())
    }

    @Test
    fun hasNoRates() {
        val source = "ï»¿<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n"
        Assertions.assertTrue(ResponseBody(source).hasNoRates())
    }

    @Test
    fun hasRates() {
        Assertions.assertFalse(ResponseBody(response).hasNoRates())
    }

    @Test
    fun clean() {
        val body = ResponseBody(response).clean()
        val expected =
                "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
                        "<currencies>\n" +
                        "  <last_update>2017-08-14</last_update>\n" +
                        "  <currency>\n" +
                        "    <name>dollar</name>\n" +
                        "    <unit>1</unit>\n" +
                        "    <currencycode>usd</currencycode>\n" +
                        "    <country>usa</country>\n" +
                        "    <rate>3.583</rate>\n" +
                        "    <change>-0.084</change>\n" +
                        "  </currency>\n" +
                        "</currencies>"
        Assertions.assertEquals(expected, body.body)
    }

    @Test
    fun getBody() {
        val body = ResponseBody(response)
        Assertions.assertEquals(response, body.body)
    }
}