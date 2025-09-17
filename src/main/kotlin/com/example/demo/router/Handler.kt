package com.example.demo.router

import com.example.demo.domain.Notification
import com.example.demo.model.dto.NotificationDTO
import com.example.demo.service.NotificationService
import com.example.notificationservice.exception.InvalidNotificationDataException
import com.example.notificationservice.exception.NotificationNotFoundException
import com.example.notificationservice.model.NotificationRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.net.URI

@Component
class NotificationHandler(
    private val notificationService: NotificationService
) {
    private fun Notification.toDTO(): NotificationDTO {
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
    fun createNotification(request: ServerRequest): ServerResponse {
            return try {
                val notificationRequest = request.body(NotificationRequest::class.java)
                val createdNotification = notificationService.createNotification(notificationRequest)
                ServerResponse.created(URI.create("/api/notifications/${createdNotification.id}"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createdNotification.toDTO())
            } catch (e: InvalidNotificationDataException) {
                ServerResponse.badRequest().body(e.message ?: "Некорректные данные для уведомления")
            } catch (e: Exception) {
                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message ?: "Внутренняя ошибка сервера")
            }
    }
    fun getNotificationById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull()
        if (id == null) {
            return ServerResponse.badRequest().body("Некорректный ID уведомления")
        }

        return try {
            val notification = notificationService.getNotificationById(id)
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(notification.toDTO())
        } catch (e: NotificationNotFoundException) {
            ServerResponse.notFound().build()
        } catch (e: Exception) {
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message ?: "Внутренняя ошибка сервера")
        }
    }
    fun getNotificationsByStatus(request: ServerRequest): ServerResponse {
        val status = request.pathVariable("status")
        return try {
            val notifications = notificationService.findNotificationsByStatus(status)
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(notifications.map { it.toDTO() })
        } catch (e: Exception) {
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message ?: "Внутренняя ошибка сервера")
        }
    }
}

