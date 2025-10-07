package com.example.demo.controller

import com.example.demo.mapper.toDTO
import com.example.demo.model.NotificationRequest
import com.example.demo.model.dto.NotificationDTO
import com.example.demo.service.NotificationService
import com.example.notificationservice.exception.InvalidNotificationDataException
import com.example.notificationservice.exception.NotificationNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationService: NotificationService,
    @Value("\${notification.max-retries:3}") private val defaultMaxRetries: Int
) {
    @PostMapping
    fun createNotification(@RequestBody request: NotificationRequest): ResponseEntity<Any> {
        return try {
            val createdNotification = notificationService.createNotification(request)
            val location: URI = UriComponentsBuilder.fromPath("/api/notifications/{id}")
                .buildAndExpand(createdNotification.id)
                .toUri()
            ResponseEntity.created(location).body(createdNotification.toDTO())
        } catch (e: InvalidNotificationDataException) {
            ResponseEntity.badRequest().body(e.message ?: "Некорректные данные для уведомления")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message ?: "Внутренняя ошибка сервера")
        }
    }
    @GetMapping("/{id}")
    fun getNotificationById(@PathVariable id: Long): ResponseEntity<NotificationDTO> {
        return try {
            val notification = notificationService.getNotificationById(id)
            ResponseEntity.ok(notification.toDTO())
        } catch (e: NotificationNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
    @GetMapping("/status/{status}")
    fun getNotificationsByStatus(@PathVariable status: String): ResponseEntity<List<NotificationDTO>> {
        return try {
            val notifications = notificationService.findNotificationsByStatus(status)
            val dtoList = notifications.map { it.toDTO() }
            ResponseEntity.ok(dtoList)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }
    @GetMapping("/pending")
    fun getPendingNotifications(): ResponseEntity<List<NotificationDTO>> {
        return try {
            val notifications = notificationService.findNotificationsByStatus("pending")
            val dtoList = notifications.map { it.toDTO() }
            ResponseEntity.ok(dtoList)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }
}
