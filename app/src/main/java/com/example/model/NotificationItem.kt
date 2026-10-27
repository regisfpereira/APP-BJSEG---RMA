package com.example.model

data class NotificationItem(
    val id: String,
    val customerId: String,
    val rmaId: String,
    val rmaNumber: String,
    val title: String,
    val message: String,
    val timestampDisplay: String,
    val isRead: Boolean = false,
    val statusTrigger: RmaStatus
)
