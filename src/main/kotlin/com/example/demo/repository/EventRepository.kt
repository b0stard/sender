package com.example.demo.repository

import com.example.demo.domain.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findByEventType(eventType: String): List<Event>
}