package com.natint.springboot.web

import com.natint.springboot.entity.UrlNotSetException
import com.natint.springboot.service.UrlService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/url")
class UrlController(private val urlService: UrlService) {

    @PostMapping("/set")
    fun set(@RequestBody url: String) {
        urlService.set(url)
    }

    @GetMapping("/get")
    @ResponseBody
    @Throws(UrlNotSetException::class)
    fun get(): String? {
        return urlService.get()
    }
}
