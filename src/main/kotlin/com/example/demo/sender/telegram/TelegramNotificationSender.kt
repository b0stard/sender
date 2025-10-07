package com.example.demo.sender

import com.example.demo.domain.Notification
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TelegramNotificationSender(
    @Value("\${telegram.bot.token}") private val botToken: String,
    @Value("\${telegram.chat.id}") private val chatId: String
) : NotificationSender {

    private val logger = LoggerFactory.getLogger(TelegramNotificationSender::class.java)
    private val client = OkHttpClient()

    override fun sendMessage(notification: Notification): Boolean {
        val apiUrl = "https://api.telegram.org/bot$botToken/sendMessage"

        val json = """
            {
                "chat_id": "$chatId",
                "text": "${notification.payload}"
            }
        """.trimIndent()

        val body = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(apiUrl)
            .post(body)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    logger.error("Ошибка при отправке Telegram уведомления (код ${response.code}): ${response.body?.string()}")
                    return false
                }
                logger.info("Отправлено Telegram уведомление: ${notification.id}")
                return true
            }
        } catch (e: Exception) {
            logger.error("Ошибка при отправке Telegram уведомления ${notification.id}: ${e.message}", e)
            return false
        }
    }
}