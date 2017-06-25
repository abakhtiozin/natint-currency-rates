package com.natint.database.entity

enum class RequestType(val id: Long) {
    NEWEST(1),
    BY_DATE(2),
    YESTERDAY(3)
}