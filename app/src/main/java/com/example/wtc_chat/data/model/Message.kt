package com.example.wtc_chat.data.model

data class Message(
    val id: String,
    val title: String?,
    val body: String,
    val sender: String,
    val timestamp: Long,
    val actions: List<MessageAction> = emptyList()
)

data class MessageAction(
    val text: String,
    val type: String, // "url", "deeplink"
    val value: String
)
