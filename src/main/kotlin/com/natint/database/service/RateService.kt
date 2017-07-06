package com.natint.database.service

import com.natint.database.entity.CurrencyRateEntity
import com.natint.database.entity.RateDateEntity
import com.natint.database.repository.CurrencyRateRepository
import com.natint.database.repository.RateDateRepository
import com.natint.model.Rates
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDate

@Component
open class RateService(
        private val currencyRateRepository: CurrencyRateRepository,
        private val rateDateRepository: RateDateRepository
) {

    fun findByDate(date: LocalDate): Rates {
        val timestamp = Timestamp.valueOf(date.atStartOfDay())
        val rateDateEntity = rateDateRepository.findByDate(timestamp)
        val currencyRateEntities = currencyRateRepository.findAllByRateDateEntity(rateDateEntity)
        return Rates.from(date, currencyRateEntities)
    }

    fun save(rates: Rates) {
        fun saveRates(timestamp: Timestamp?, rates: Rates) {
            var rateDateEntity = RateDateEntity(date = timestamp)
            rateDateEntity = rateDateRepository.save(rateDateEntity)
            val currencyRateEntities = rates.rates.map {
                CurrencyRateEntity(
                        currencyCode = it.currencyCode,
                        rate = it.rate,
                        rateDateEntity = rateDateEntity
                )
            }
            currencyRateRepository.save(currencyRateEntities)
        }

        val timestamp = Timestamp.valueOf(rates.date.atStartOfDay())
        val dbRateDate: RateDateEntity? = rateDateRepository.findByDate(timestamp)
        if (dbRateDate == null) {
            saveRates(timestamp, rates)
        } else {
            val currencyRateEntities = currencyRateRepository.findAllByRateDateEntity(dbRateDate)
            currencyRateRepository.delete(currencyRateEntities)
            rateDateRepository.delete(dbRateDate)
            saveRates(timestamp, rates)
        }
    }
}