package com.natint.database.service

import com.natint.database.entity.RequestLog
import com.natint.database.entity.RequestType
import com.natint.database.repository.RequestLogRepository
import com.natint.database.repository.RequestTypeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
open class RequestService(
        private val requestLogRepository: RequestLogRepository,
        private val requestTypeRepository: RequestTypeRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun saveRequest(requestType: RequestType) {
        val requestTypeEntity = requestTypeRepository.findOne(requestType.id)
        val requestLog = requestLogRepository.save(
                RequestLog(
                        requestTypeEntity = requestTypeEntity,
                        requestDate = Timestamp.valueOf(LocalDateTime.now())
                )
        )
        logger.info("$requestLog")
    }
}
