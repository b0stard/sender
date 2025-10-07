package com.example.demo.service

import com.example.demo.domain.Notification
import com.example.demo.model.NotificationRequest
import com.example.demo.repository.NotificationRepository
import com.example.notificationservice.exception.InvalidNotificationDataException
import com.example.notificationservice.exception.NotificationServiceException
import com.example.notificationservice.model.dto.EventDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import kotlin.random.Random

@Service
 class NotificationService(
    private val notificationRepository: NotificationRepository,
    @Value("\${notification.max-retries:3}") private val maxRetries: Int,
    @Value("\${notification.retry-delay-seconds:10}") private val retryDelaySeconds: Long
) { private val logger = LoggerFactory.getLogger(NotificationService::class.java)
    fun processEventAndGenerateNotifications(eventDto: EventDto) {
        logger.info("Обработка события: ${eventDto.eventType}")
        val eventId = eventDto.eventId ?: 0L
        try {
            when (eventDto.eventType) {
                "user_registered" -> {
                    createNotificationIfMissing(NotificationRequest(eventId, "email", "Добро пожаловать, ${eventDto.eventData}!")
                    )
                    createNotificationIfMissing(
                        NotificationRequest(eventId, "telegram", "Привет! Ваша регистрация прошла успешно.")
                    )
                }
                "order_placed" -> {
                    createNotificationIfMissing(
                        NotificationRequest(eventId, "email", "Ваш заказ ${eventDto.eventData} размещен.")
                    )
                }
                else -> {
                    logger.warn("Нет логики для создания уведомлений для типа события: ${eventDto.eventType}")
                }
            }
        } catch (e: InvalidNotificationDataException) {
            logger.error("Ошибка при генерации уведомления для события ${eventDto.eventType}: ${e.message}", e)
        } catch (e: Exception) {
            logger.error("Неожиданная ошибка при обработке события ${eventDto.eventType}: ${e.message}", e)
        }
    }
    private fun createNotificationIfMissing(request: NotificationRequest) {
        val existingNotifications =
            notificationRepository.findByEventIdAndChannelType(request.eventId, request.channelType)
        if (existingNotifications.isNotEmpty()) {
            logger.info("Уведомление для события ${request.eventId} и канала ${request.channelType} уже существует. Пропуск создания.")
            return
        }
        createNotification(request)
    }

    fun createNotification(request: NotificationRequest): Notification {
        if (request.eventId <= 0) throw InvalidNotificationDataException("ID события должно быть положительным")
        if (request.channelType.isBlank()) throw InvalidNotificationDataException("Тип канала не может быть пустым")
        if (request.payload.isBlank()) throw InvalidNotificationDataException("Содержимое уведомления не может быть пустым")

        val notification = Notification(
            eventId = request.eventId,
            channelType = request.channelType,
            payload =   request.payload,
            status = "pending",
            attempts = 0,
            createdAt = Instant.now(),
            subject = "Test body",
            recipient = "test@example.com"

        )
        return notificationRepository.save(notification)
    }
   fun attemptSendNotification(notificationId: Long): Boolean {
        val notification = notificationRepository.findById(notificationId).orElseThrow {
            NotificationServiceException("Уведомление с ID $notificationId не найдено для попытки отправки")
        }
        if (notification.status == "sent") {
            logger.info("Уведомление ${notification.id} уже отправлено. Пропуск попытки отправки.")
            return true
        }


        if (notification.attempts >= maxRetries) {
            logger.warn("Уведомление ${notification.id} достигло максимального количества попыток ($maxRetries). Статус 'failed'.")
            notification.status = "failed"
            notificationRepository.save(notification)
            return false
        }

        val currentAttempt = notification.attempts + 1
        logger.info("Попытка отправки уведомления ${notification.id} (Попытка $currentAttempt из $maxRetries) по каналу ${notification.channelType}")

        try {
            val success = simulateSend(notification)

            notification.attempts = currentAttempt
            notification.lastAttemptAt = Instant.now()

            if (success) {
                notification.status = "sent"
                logger.info("Уведомление ${notification.id} успешно отправлено.")
            } else {
                logger.warn("Отправка уведомления ${notification.id} не удалась. Повторная попытка позже.")
            }
            notificationRepository.save(notification)
            return success
        } catch (e: Exception) {
            logger.error(
                "Исключение при отправке уведомления ${notification.id} (Попытка $currentAttempt): ${e.message}",
                e
            )
            notification.status = "failed"
            notificationRepository.save(notification)
            return false
        }
    }

    private fun simulateSend(notification: Notification): Boolean {
        Thread.sleep(100)
        val success = Random.nextDouble() > 0.3
        return success
    }

    fun findNotificationsByStatus(status: String): List<Notification> {
        return notificationRepository.findByStatus(status)
    }

    fun getNotificationById(id: Long): Notification {
        return notificationRepository.findById(id).orElseThrow {
            NotificationServiceException("Уведомление с ID $id не найдено")
        }
    }
}