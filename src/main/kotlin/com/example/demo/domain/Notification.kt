package com.example.demo.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
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

@Column(nullable = false, length = 20)
var status: String = "pending",

@Column(nullable = false, length = 50)
var channelType: String = "",

@Column(nullable = false, columnDefinition = "TEXT")
var payload: String = "",

@field:NotBlank(message = "Тема не может быть пустой")
@field:Size(min = 1, max = 255, message = "Тема должна содержать от 1 до 255 символов")
val subject: String,

@field:NotBlank(message = "Получатель не может быть пустым")
@field:Email(message = "Получатель должен быть валидным email-адресом")
val recipient: String,

@Column(nullable = false)
var attempts: Int = 0,

@CreationTimestamp
@Column(nullable = false, updatable = false)
val createdAt: Instant = Instant.now(),

var lastAttemptAt: Instant? = null
)