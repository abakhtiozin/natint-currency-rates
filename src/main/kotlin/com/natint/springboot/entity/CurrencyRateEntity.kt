package com.natint.springboot.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "currency_rate")
data class CurrencyRateEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", length = 6, nullable = false)
        var id: Long? = null,
        @ManyToOne
        @JoinColumn(name = "currency_id")
        var currencyEntity: CurrencyEntity?,
        @Column(name = "rate")
        var rate: Double,
        @ManyToOne
        @JoinColumn(name = "rate_date_id")
        var rateDateEntity: RateDateEntity? = null
)