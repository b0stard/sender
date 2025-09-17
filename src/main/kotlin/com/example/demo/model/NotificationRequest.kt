package com.example.notificationservice.model

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class NotificationRequest(
    @field:Positive(message = "ID event должен быть положительным числом")
    val eventId: Long,

    @field:NotBlank(message = "Тип канала не может быть пустым")
    val channelType: String,

    val payload: String
)