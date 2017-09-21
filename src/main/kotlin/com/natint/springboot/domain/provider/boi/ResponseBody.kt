package com.natint.springboot.domain.provider.boi

internal class ResponseBody(val body: String) {
    fun isEmpty() = body.isEmpty()
    fun isNotEmpty() = body.isNotEmpty()
    fun hasNoRates() = !body.contains("currencies", true)
    fun clean(): ResponseBody {
        val regex = Regex("\\P{InBasic_Latin}")
        return ResponseBody(body.replace(regex, ""))
    }

    override fun toString() = body
}