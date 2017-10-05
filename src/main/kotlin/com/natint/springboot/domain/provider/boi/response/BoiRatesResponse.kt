package com.natint.springboot.domain.provider.boi.response

import com.natint.springboot.domain.provider.boi.ResponseBody
import java.time.LocalDate

internal class BoiRatesResponse(val date: LocalDate, val body: ResponseBody)
