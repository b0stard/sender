package com.example.demo.service

import com.example.demo.domain.Event
import com.example.demo.repository.EventRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class EventService (
    private val eventRepository: EventRepository
)
{
    fun findByEventType(eventType: String): List<Event>{
        return eventRepository.findByEventType(eventType)
    }
}