package com.natint.springboot.domain.provider.boi.xml.mapping

import org.jonnyzzz.kotlin.xml.bind.XText
import org.jonnyzzz.kotlin.xml.bind.jdom.JXML

class KCurrencies {
    var date by JXML / "LAST_UPDATE" / XText
}