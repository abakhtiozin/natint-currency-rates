package com.natint.springboot.service

import com.natint.springboot.entity.ProviderEntity
import com.natint.springboot.repository.ProviderRepository
import org.springframework.stereotype.Service

@Service
open class ProviderService(private val providerRepository: ProviderRepository) {

    open fun get(name: String): ProviderEntity{
        return providerRepository.findByName(name)
    }
}
