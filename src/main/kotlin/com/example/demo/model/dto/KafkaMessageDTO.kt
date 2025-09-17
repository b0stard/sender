package com.example.notificationservice.model.dto

data class KafkaMessageDto(
    val key: String,
    val value: String
)