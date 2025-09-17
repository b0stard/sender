package com.example.demo.router


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.router

@Configuration
class NotificationRouter(private val notificationHandler: NotificationHandler) {

    @Bean
    fun notificationRoutes() = router {
        "/api/notifications".nest {
            POST("/", accept(MediaType.APPLICATION_JSON), notificationHandler::createNotification)
            GET("/{id}", accept(MediaType.APPLICATION_JSON), notificationHandler::getNotificationById)
            GET("/status/{status}", accept(MediaType.APPLICATION_JSON), notificationHandler::getNotificationsByStatus)
        }
    }
}