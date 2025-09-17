package com.example.notificationservice.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class EventDto(
    @field:NotBlank(message = "Event не может быть пустым")
    @field:Size(max = 255, message = "Event не может превышать 255 символов")
    val eventType: String,

    @field:NotBlank(message = "Event данные не могут быть пустыми")
    val eventData: String,

    val eventId: Long? = null
)