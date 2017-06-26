package com.natint.database.entity

import org.hibernate.annotations.GenericGenerator
import java.sql.Timestamp

import javax.persistence.*

@Entity
@Table(name = "request_log")
data class RequestLog(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @GenericGenerator(name = "increment", strategy = "increment")
        @Column(name = "id", length = 6, nullable = false)
        var id: Int? = null,
        @ManyToOne
        @JoinColumn(name = "request_type_id")
        var requestTypeEntity: RequestTypeEntity? = null,
        @Column
        var requestDate: Timestamp? = null
)