package com.natint.springboot.repository

import com.natint.springboot.entity.CurrencyRateEntity
import com.natint.springboot.entity.RateDateEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Timestamp

interface RateDateRepository : JpaRepository<RateDateEntity, Long> {
    fun findByDate(timestamp: Timestamp): RateDateEntity
}

interface CurrencyRateRepository : JpaRepository<CurrencyRateEntity, Long> {
    fun findAllByRateDateEntity(rateDateEntity: RateDateEntity): List<CurrencyRateEntity>
}