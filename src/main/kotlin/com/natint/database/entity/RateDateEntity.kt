package com.natint.database.entity

import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "rate_date")
data class RateDateEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", length = 6, nullable = false)
        var id: Long? = null,
        @Column(name = "date")
        var requestDate: Timestamp? = null
)