package com.natint.springboot.service

import com.natint.springboot.domain.rates.Rates
import com.natint.springboot.entity.CurrencyRateEntity
import com.natint.springboot.entity.RateDateEntity
import com.natint.springboot.repository.CurrencyRateRepository
import com.natint.springboot.repository.RateDateRepository
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDate

@Service
open class RateService(
        private val currencyRateRepository: CurrencyRateRepository,
        private val rateDateRepository: RateDateRepository
) {

    fun find(date: LocalDate): Rates {
        val timestamp = Timestamp.valueOf(date.atStartOfDay())
        val rateDateEntity = rateDateRepository.findByDate(timestamp)
        val currencyRateEntities = currencyRateRepository.findAllByRateDateEntity(rateDateEntity)
        return Rates.from(date, currencyRateEntities)
    }

    fun save(rates: Rates) {
        fun Rates.saveRates(timestamp: Timestamp?) {
            var rateDateEntity = RateDateEntity(date = timestamp)
            rateDateEntity = rateDateRepository.save(rateDateEntity)
            val currencyRateEntities = this.values.map {
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
            rates.saveRates(timestamp)
        } else {
            delete(rates.date)
            rates.saveRates(timestamp)
        }
    }

    fun delete(date: LocalDate) {
        val timestamp = Timestamp.valueOf(date.atStartOfDay())
        val dbRateDate: RateDateEntity = rateDateRepository.findByDate(timestamp)
        val currencyRateEntities = currencyRateRepository.findAllByRateDateEntity(dbRateDate)
        currencyRateRepository.delete(currencyRateEntities)
        rateDateRepository.delete(dbRateDate)
    }

    fun findAll(): List<Rates> {
        val all = currencyRateRepository.findAll(Sort(DESC, "id"))
        return all.filter {
            val rateDateEntity = it.rateDateEntity
            val date = rateDateEntity?.date
            date != null
        }.groupBy {
            val timestamp = it.rateDateEntity?.date
            timestamp?.toLocalDateTime()?.toLocalDate()
        }.map {
            Rates.from(it.key!!, it.value)
        }
    }
}