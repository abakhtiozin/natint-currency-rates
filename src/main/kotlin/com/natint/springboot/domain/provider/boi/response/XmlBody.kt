package com.natint.springboot.domain.provider.boi.response

import com.natint.springboot.domain.provider.boi.ResponseBody
import com.natint.springboot.domain.provider.boi.xml.mapping.Currencies
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.io.IOException
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException

internal class XmlBody(private val xmlBody: ResponseBody) {
    private val encoding = "UTF-8"
    private val logger = LoggerFactory.getLogger(this.javaClass)

    internal fun unmarshal(): Currencies = try {
        val inputStream = IOUtils.toInputStream(xmlBody.toString(), encoding)
        val jaxbContext = JAXBContext.newInstance(Currencies::class.java)
        val jaxbUnmarshaller = jaxbContext.createUnmarshaller()
        jaxbUnmarshaller.unmarshal(inputStream) as Currencies
    } catch (ex: Exception) {
        val currencies = Currencies()
        when (ex) {
            is IOException,
            is JAXBException -> {
                logger.info("error occurred " + ex.toString())
                currencies
            }
            else -> currencies
        }
    }
}
