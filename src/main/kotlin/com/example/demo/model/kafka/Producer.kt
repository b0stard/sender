package com.example.demo.model.kafka


import com.example.notificationservice.model.dto.EventDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class EventProducer(
    private val kafkaTemplate: KafkaTemplate<String, EventDto>,
    @Value("\${kafka.topic.processed-events}") private val processedEventsTopic: String
) {
    private val logger = LoggerFactory.getLogger(EventProducer::class.java)

    fun sendProcessedEvent(event: EventDto, key: String) {
        try {
            logger.info("Отправка обработанного event в раздел Kafka '$processedEventsTopic' с ключа'$key'")

            kafkaTemplate.send(processedEventsTopic, key, event)
            logger.info("Event отправлено успешно.")
        } catch (e: Exception) {
            logger.error("Ошибка при отправке event в Kafka: ${e.message}", e)
        }
    }
}