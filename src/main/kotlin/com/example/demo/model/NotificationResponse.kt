package com.example.notificationservice.model

data class NotificationResponse(
    val id: Long,
    val eventId: Long,
    val channelType: String,
    val status: String
)