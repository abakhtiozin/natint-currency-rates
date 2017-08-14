package com.natint.springboot.domain.rates

interface Representable {
    fun represent(): Collection<Any?>
}