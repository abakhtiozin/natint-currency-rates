package com.natint.database.repository

import com.natint.database.entity.CurrencyRateEntity
import com.natint.database.entity.RateDateEntity
import com.natint.database.entity.RequestLog
import com.natint.database.entity.RequestTypeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RequestLogRepository : JpaRepository<RequestLog, Long>

interface RequestTypeRepository : JpaRepository<RequestTypeEntity, Long>

interface RateDateRepository : JpaRepository<RateDateEntity, Long>

interface CurrencyRateRepository : JpaRepository<CurrencyRateEntity, Long>