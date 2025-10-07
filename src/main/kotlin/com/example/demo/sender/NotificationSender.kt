package com.example.demo.sender

import com.example.demo.domain.Notification

interface NotificationSender {
    fun sendMessage(notification: Notification): Boolean
}