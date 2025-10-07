package com.example.demo.sender.email

import com.example.demo.domain.Notification
import com.example.demo.sender.NotificationSender
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class EmailNotificationSender(
    private val javaMailSender: JavaMailSender,
    @Value("\${spring.mail.username}") private val senderEmail: String
) : NotificationSender {

    private val logger = LoggerFactory.getLogger(EmailNotificationSender::class.java)

    override fun sendMessage(notification: Notification): Boolean {
        try {
            val message = SimpleMailMessage()
            message.setTo("recipient@gmail.com")
            message.setFrom(senderEmail)
            message.setSubject("Уведомление от NotificationService")
            message.setText(notification.payload)
            javaMailSender.send(message)
            logger.info("Уведомление успешно отправлено: ${notification.id}")
            return true
        } catch (e: Exception) {
            logger.error("Ошибка при отправке email уведомления ${notification.id}: ${e.message}", e)
            return false
        }
    }
}