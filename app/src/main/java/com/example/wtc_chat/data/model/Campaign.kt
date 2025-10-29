package com.example.wtc_chat.data.model

data class Campaign(
    val title: String = "",
    val message: String = "",
    val segment: String = "all",
    val sentAt: Long = System.currentTimeMillis()
)
