package com.natint.controller

import com.natint.database.service.RateService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/db")
class DatabaseRatesController(val rateService: RateService) {

    @RequestMapping("/getAll", method = arrayOf(GET), produces = arrayOf("application/json"))
    @ResponseBody fun getAll() = rateService.findAll()
}
