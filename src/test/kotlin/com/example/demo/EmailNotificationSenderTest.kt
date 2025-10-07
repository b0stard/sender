package com.example.demo

import com.example.demo.domain.Notification
import com.example.demo.sender.email.EmailNotificationSender
import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.mail.javamail.JavaMailSender
import java.time.Instant


class EmailNotificationSenderTest {

    @Test
    fun `sendEmail should call javaMailSender send`() {

        val mailSender = mock<JavaMailSender>()
        val fromAddress = "dipper7979@gmail.com"
        val emailNotificationSender = EmailNotificationSender(mailSender, fromAddress)

        val to = "dipper7979@gmail.com"
        val subject = "Test subject"
        val body = "Test body"
        val notification = Notification(
            id = null,
            eventId = 123L,
            channelType = "EMAIL",
            payload = body,
            status = "pending",
            attempts = 0,
            createdAt = java.time.Instant.now(),
            lastAttemptAt = null,
            subject = "Test body",
            recipient = "test@example.com"
        )
        val mimeMessage = mock<MimeMessage>()
        whenever(mailSender.createMimeMessage()).thenReturn(mimeMessage)
        emailNotificationSender.sendMessage(notification)
        verify(mailSender).send(mimeMessage)
    }
}