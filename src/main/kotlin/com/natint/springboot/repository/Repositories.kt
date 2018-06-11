package com.natint.springboot.repository

import com.natint.springboot.entity.CurrencyEntity
import com.natint.springboot.entity.CurrencyRateEntity
import com.natint.springboot.entity.ProviderEntity
import com.natint.springboot.entity.RateDateEntity
import com.natint.springboot.entity.UrlEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Timestamp

interface RateDateRepository : JpaRepository<RateDateEntity, Long> {
    fun findByDate(timestamp: Timestamp): RateDateEntity
}

interface CurrencyRateRepository : JpaRepository<CurrencyRateEntity, Long> {
    fun findAllByRateDateEntity(rateDateEntity: RateDateEntity): List<CurrencyRateEntity>
}

interface UrlRepository : JpaRepository<UrlEntity, Long>
interface CurrencyRepository : JpaRepository<CurrencyEntity, Long>
interface ProviderRepository : JpaRepository<ProviderEntity, Long> {
    fun findByName(name: String): ProviderEntity
}