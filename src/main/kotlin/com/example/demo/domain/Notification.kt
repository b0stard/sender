package com.example.demo.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "notifications")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var eventId: Long = 0,

    @Column(nullable = false, length = 50)
    var channelType: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var payload: String = "",

    @Column(nullable = false, length = 20)
    var status: String = "pending",

    @Column(nullable = false)
    var attempts: Int = 0,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    var lastAttemptAt: Instant? = null
)