package com.natint.springboot.service

import com.natint.springboot.entity.UrlEntity
import com.natint.springboot.entity.UrlNotSetException
import com.natint.springboot.repository.UrlRepository
import org.springframework.stereotype.Service

@Service
open class UrlService(private val urlRepository: UrlRepository) {
    open fun set(url: String) {
        urlRepository.deleteAll()
        urlRepository.save(UrlEntity(url = url))
    }

    @Throws(UrlNotSetException::class)
    open fun get(): UrlEntity? {
        return try {
            urlRepository.getOne(1)
        } catch (e: Exception) {
            throw UrlNotSetException()
        }
    }
}
