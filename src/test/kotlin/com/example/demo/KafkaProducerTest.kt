package com.example.demo

import com.example.demo.model.kafka.EventProducer
import com.example.notificationservice.model.dto.EventDto
import org.apache.kafka.clients.producer.KafkaProducer
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.kafka.core.KafkaTemplate

class KafkaProducerTest{
     @Test
     fun `sendMessage should call kafkaTemplate send`() {
         val kafkaTemplate = mock<KafkaTemplate<String, EventDto>>()
         val topic = "test-topic"
         val eventProducer = EventProducer(kafkaTemplate, topic)

         val event = EventDto("Test Event", "Test Data")
         val key = "event-key"

         eventProducer.sendProcessedEvent(event, key)

         val topicCaptor = argumentCaptor<String>()
         val keyCaptor = argumentCaptor<String>()
         val eventCaptor = argumentCaptor<EventDto>()

         verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), eventCaptor.capture())

         assert(topicCaptor.firstValue == topic)
         assert(keyCaptor.firstValue == key)
         assert(eventCaptor.firstValue == event)
     }
 }