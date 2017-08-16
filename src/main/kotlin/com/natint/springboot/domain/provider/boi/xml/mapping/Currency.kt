package com.natint.springboot.domain.provider.boi.xml.mapping

class Currency {
    private var currencycode: String = ""
    private var rate: String = ""

    constructor()

    constructor(currencycode: String, rate: String) {
        this.currencycode = currencycode
        this.rate = rate
    }

    fun getCurrencycode() = currencycode
    fun getRate() = rate
    fun setCurrencycode(currencycode: String) {
        this.currencycode = currencycode
    }

    fun setRate(rate: String) {
        this.rate = rate
    }
}
