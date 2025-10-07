package com.example.demo.model.kafka

import com.example.demo.service.NotificationService
import com.example.notificationservice.model.dto.EventDto
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class EventConsumer(
    private val notificationService: NotificationService
) {
    private val logger = LoggerFactory.getLogger(EventConsumer::class.java)

    @KafkaListener(
        topics = ["\${kafka.topic.processed-events}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun listenToEvents(eventDto: EventDto) {
        logger.info("Получено событие: ${eventDto.eventType} (ID события: ${eventDto.eventId})")

        try {
            notificationService.processEventAndGenerateNotifications(eventDto)
            logger.info("Событие '${eventDto.eventType}' обработано.")
        } catch (e: Exception) {
            logger.error("Ошибка при обработке события '${eventDto.eventType}': ${e.message}", e)
        }
    }
}