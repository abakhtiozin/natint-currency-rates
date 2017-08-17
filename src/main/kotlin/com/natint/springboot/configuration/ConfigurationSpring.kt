package com.natint.springboot.configuration

import com.natint.springboot.domain.provider.DatabaseRatesProvider
import com.natint.springboot.domain.provider.RatesProvider
import com.natint.springboot.domain.provider.boi.BoiRates
import com.natint.springboot.service.RateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.client.RestTemplate

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = arrayOf("com.natint.*"))
@PropertySource("classpath:application.properties")
open class ConfigurationSpring {

    @Autowired
    private lateinit var env: Environment

    @Autowired
    private lateinit var rateService: RateService

    @Bean(name = arrayOf("boiRates"))
    @DependsOn(value = "restTemplate")
    open fun boiRates(): RatesProvider {
        val url = env.getProperty("provider.boi.url")
        val restTemplate = restTemplate()
        (restTemplate.requestFactory as SimpleClientHttpRequestFactory).setConnectTimeout(30000)
        val ratesProvider = BoiRates(restTemplate, url)
        return DatabaseRatesProvider(ratesProvider, rateService)
    }

    @Bean(name = arrayOf("restTemplate"))
    open fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean open fun persistenceExceptionTranslationPostProcessor() = PersistenceExceptionTranslationPostProcessor()
}
