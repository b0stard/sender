package com.example.demo.repository

import com.example.demo.domain.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, Long> {
    fun findByStatus(status: String): List<Notification>
    fun findByEventIdAndChannelType(eventId: Long, channelType: String): List<Notification>
    fun findByStatusAndAttemptsLessThan(status: String, maxAttempts: Int): List<Notification>
}