package com.natint.database.entity

import javax.persistence.*

@Entity
@Table(name = "request_type")
data class RequestTypeEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", length = 6, nullable = false)
        var id: Long? = null,
        @Column(name = "type")
        @Enumerated(EnumType.STRING)
        var requestType: RequestType
)
