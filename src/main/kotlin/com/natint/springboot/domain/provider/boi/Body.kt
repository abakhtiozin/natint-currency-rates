package com.natint.springboot.domain.provider.boi

internal class Body(val body: String) {
    fun isEmpty() = body.isEmpty()
    fun isNotEmpty() = body.isNotEmpty()
    fun hasNoRates() = !body.contains("currencies", true)
    fun clean(): Body {
        val regex = Regex("\\P{InBasic_Latin}")
        return Body(body.replace(regex, ""))
    }

    override fun toString() = body
}