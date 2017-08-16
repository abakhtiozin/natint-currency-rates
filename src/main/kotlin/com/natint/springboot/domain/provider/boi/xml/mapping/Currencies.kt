package com.natint.springboot.domain.provider.boi.xml.mapping

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class Currencies {

    private var last_update: String = ""
    private var currencies: List<Currency> = mutableListOf()

    constructor()

    constructor(last_update: String, currencies: List<Currency>) {
        this.last_update = last_update
        this.currencies = currencies
    }

    @XmlElement
    fun getLast_update(): String = last_update

    fun setLast_update(last_update: String) {
        this.last_update = last_update
    }

    @XmlElement(name = "currency")
    fun getCurrencies(): List<Currency> {
        return currencies
    }

    fun setCurrencies(currencies: List<Currency>) {
        this.currencies = currencies
    }
}