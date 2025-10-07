package com.example.demo.mapper
import com.example.demo.domain.Notification

import com.example.demo.model.dto.NotificationDTO

fun Notification.toDTO(): NotificationDTO {
    return NotificationDTO(
        id = this.id,
        eventId = this.eventId,
        channelType = this.channelType,
        payload = this.payload,
        status = this.status,
        attempts = this.attempts,
        createdAt = this.createdAt,
        lastAttemptAt = this.lastAttemptAt
    )
}