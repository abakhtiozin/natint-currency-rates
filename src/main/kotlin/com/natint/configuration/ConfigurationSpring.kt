package com.natint.configuration

import com.natint.model.provider.RatesProvider
import com.natint.model.provider.boi.BoiRates
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.*
import org.springframework.core.env.Environment
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
@ComponentScan(basePackages = arrayOf("com.natint.*"))
@PropertySource("classpath:config.properties")
open class ConfigurationSpring {

    @Autowired
    private lateinit var env: Environment

    @Bean(name = arrayOf("boiRates"))
    @DependsOn(value = "restTemplate")
    open fun boiRates(): RatesProvider {
        val url = env.getProperty("provider.boi.url")
        val restTemplate = restTemplate()
        (restTemplate.requestFactory as SimpleClientHttpRequestFactory).setConnectTimeout(30000)
        val ratesProvider = BoiRates(restTemplate, url)
        return ratesProvider
    }

    @Bean(name = arrayOf("restTemplate"))
    open fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
