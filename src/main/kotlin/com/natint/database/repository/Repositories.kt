package com.natint.database.repository

import com.natint.database.entity.CurrencyRateEntity
import com.natint.database.entity.RateDateEntity
import com.natint.database.entity.RequestLog
import com.natint.database.entity.RequestTypeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Timestamp

interface RequestLogRepository : JpaRepository<RequestLog, Long>

interface RequestTypeRepository : JpaRepository<RequestTypeEntity, Long>

interface RateDateRepository : JpaRepository<RateDateEntity, Long> {
    fun findByDate(timestamp: Timestamp): RateDateEntity
}

interface CurrencyRateRepository : JpaRepository<CurrencyRateEntity, Long> {
    fun findAllByRateDateEntity(rateDateEntity: RateDateEntity): List<CurrencyRateEntity>
}