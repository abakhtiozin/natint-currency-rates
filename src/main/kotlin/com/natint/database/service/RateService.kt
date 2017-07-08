package com.natint.database.service

import com.beust.klaxon.JsonArray
import com.natint.database.entity.CurrencyRateEntity
import com.natint.database.entity.RateDateEntity
import com.natint.database.repository.CurrencyRateRepository
import com.natint.database.repository.RateDateRepository
import com.natint.model.Rates
import com.natint.model.json
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
            delete(rates.date)
            saveRates(timestamp, rates)
        }
    }

    fun delete(date: LocalDate) {
        val timestamp = Timestamp.valueOf(date.atStartOfDay())
        val dbRateDate: RateDateEntity = rateDateRepository.findByDate(timestamp)
        val currencyRateEntities = currencyRateRepository.findAllByRateDateEntity(dbRateDate)
        currencyRateRepository.delete(currencyRateEntities)
        rateDateRepository.delete(dbRateDate)
    }

    fun findAll(): List<JsonArray<Any?>> {
        val all = currencyRateRepository.findAll(Sort(DESC, "id"))
        return all.filter {
            val rateDateEntity = it.rateDateEntity
            val date = rateDateEntity?.date
            date != null
        }.groupBy {
            val timestamp = it.rateDateEntity?.date
            timestamp?.toLocalDateTime()?.toLocalDate()
        }.map { Rates.from(it.key!!, it.value).json() }
    }
}