package com.natint.springboot.service

import com.natint.springboot.domain.rates.Rate
import com.natint.springboot.entity.CurrencyEntity
import com.natint.springboot.repository.CurrencyRepository
import org.springframework.stereotype.Service

@Service
open class CurrencyService(private val currencyRepository: CurrencyRepository) {

    open fun count(): Int {
        return currencyRepository.findAll().size
    }

    open fun update(mapper: () -> List<Rate>) {
        val dbList = currencyRepository.findAll()
        val providerCurrency = mapper().map {
            CurrencyEntity(code = it.code, number = it.number)
        }
        for (currencyEntity in providerCurrency) {
            if (currencyEntity !in dbList) {
                currencyRepository.save(currencyEntity)
            }
        }
    }
}
