package com.example.demo.sender

import com.example.demo.domain.Notification

interface NotificationSender {
    fun send(notification: Notification): Boolean
}