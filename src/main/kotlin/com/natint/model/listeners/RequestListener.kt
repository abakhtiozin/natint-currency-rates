package com.natint.model.listeners

import com.natint.database.entity.RequestLog
import com.natint.database.entity.RequestType
import com.natint.database.repository.RequestLogRepository
import com.natint.database.repository.RequestTypeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDateTime

@Component
open class RequestListener(
        val requestLogRepository: RequestLogRepository,
        val requestTypeRepository: RequestTypeRepository
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun notify(requestType: RequestType) {
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
