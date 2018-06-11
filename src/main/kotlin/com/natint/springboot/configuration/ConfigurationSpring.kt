package com.natint.springboot.configuration

import com.natint.springboot.domain.provider.DatabaseRatesProvider
import com.natint.springboot.domain.provider.RatesProvider
import com.natint.springboot.domain.provider.boi.BoiRates
import com.natint.springboot.domain.provider.nbu.NbuRates
import com.natint.springboot.service.ProviderService
import com.natint.springboot.service.RateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.PropertySource
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.client.RestTemplate

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = ["com.natint.*"])
@PropertySource("classpath:application.properties")
open class ConfigurationSpring {

    @Autowired
    private lateinit var rateService: RateService
    @Autowired
    private lateinit var providerService: ProviderService

    @Bean(name = ["boiRates"])
    @DependsOn(value = ["restTemplate"])
    open fun boiRates(): RatesProvider {
        val provider = providerService.get("boi")
        val ratesProvider = BoiRates(restTemplate(), provider)
        return DatabaseRatesProvider(ratesProvider, rateService)
    }

    @Bean(name = ["nbuRates"])
    @DependsOn(value = ["restTemplate"])
    open fun nbuRates(): RatesProvider {
        val provider = providerService.get("nbu")
        val ratesProvider = NbuRates(restTemplate(), provider)
        return DatabaseRatesProvider(ratesProvider, rateService)
    }

    @Bean(name = ["restTemplate"])
    open fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        val factory = restTemplate.requestFactory as SimpleClientHttpRequestFactory
        factory.setConnectTimeout(30000)
        return restTemplate
    }

    @Bean
    open fun persistenceExceptionTranslationPostProcessor(): PersistenceExceptionTranslationPostProcessor {
        return PersistenceExceptionTranslationPostProcessor()
    }
}
