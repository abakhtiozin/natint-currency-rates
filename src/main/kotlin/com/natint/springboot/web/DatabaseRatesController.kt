package com.natint.springboot.web

import com.natint.springboot.service.RateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/db")
class DatabaseRatesController(val rateService: RateService) {

    @GetMapping("/getAll")
    @ResponseBody fun getAll() = rateService.findAll()
}
