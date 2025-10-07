package com.example.demo.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant


data class NotificationDTO(
    val id: Long? = null,
    @JsonProperty("event_id")
    val eventId: Long = 0,
    val channelType: String = "",
    val payload: String = "",
    val status: String = "pending",
    val attempts: Int = 0,
    val createdAt: Instant = Instant.now(),
    val lastAttemptAt: Instant? = null,
)