package com.example.notificationservice.exception


open class NotificationServiceException(message: String? = null, cause: Throwable? = null) :
    RuntimeException(message, cause)

class NotificationNotFoundException(message: String? = null, cause: Throwable? = null) :
    NotificationServiceException(message, cause)

class InvalidNotificationDataException(message: String? = null, cause: Throwable? = null) :
    NotificationServiceException(message, cause)


class NotificationSendingException(message: String? = null, cause: Throwable? = null) :
    NotificationServiceException(message, cause)