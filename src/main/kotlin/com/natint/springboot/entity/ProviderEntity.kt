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
@Table(name = "provider")
data class ProviderEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", length = 6, nullable = false)
        var id: Long? = null,
        @Column(name = "name")
        var name: String? = null,
        @ManyToOne
        @JoinColumn(name = "currency_code_id")
        var currency: CurrencyEntity? = null,
        @ManyToOne
        @JoinColumn(name = "url_id")
        var urlEntity: UrlEntity? = null
)
