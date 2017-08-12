package com.natint.springboot.entity

import com.natint.springboot.domain.CurrencyCode
import javax.persistence.*

@Entity
@Table(name = "currency_rate")
data class CurrencyRateEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", length = 6, nullable = false)
        var id: Long? = null,
        @Column(name = "currency")
        @Enumerated(EnumType.STRING)
        var currencyCode: CurrencyCode,
        @Column(name = "rate")
        var rate: Double,
        @ManyToOne
        @JoinColumn(name = "rate_date_id")
        var rateDateEntity: RateDateEntity? = null
)