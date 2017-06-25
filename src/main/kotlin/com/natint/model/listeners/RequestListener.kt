package com.natint.model.listeners

import com.natint.database.entity.RequestLog
import com.natint.database.entity.RequestType
import com.natint.database.repository.RequestLogRepository
import com.natint.database.repository.RequestTypeRepository
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDateTime

@Component
open class RequestListener(val requestLogRepository: RequestLogRepository,val requestTypeRepository: RequestTypeRepository) {
    fun notify(requestType: RequestType) {
        val requestTypeEntity = requestTypeRepository.findOne(requestType.id)
        requestLogRepository.save(
                RequestLog(
                        requestTypeEntity = requestTypeEntity,
                        requestDate = Timestamp.valueOf(LocalDateTime.now())
                )
        )
    }
}
