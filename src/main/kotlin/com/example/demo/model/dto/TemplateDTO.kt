package com.example.notificationservice.model.dto

import jakarta.validation.constraints.NotBlank

data class TemplateDto(
    @field:NotBlank(message = "Channel канал не может быть пустым")
    val channelType: String,

    @field:NotBlank(message = "Template текст не может быть пустым")
    val templateText: String
)