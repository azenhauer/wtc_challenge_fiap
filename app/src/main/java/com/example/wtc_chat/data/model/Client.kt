package com.example.wtc_chat.data.model

import com.google.firebase.firestore.DocumentId

data class Client(
    @DocumentId val id: String = "",
    val email: String = "",
    val name: String = "",
    val tags: List<String> = emptyList(),
    val score: Int = 0,
    val status: String = "",
    val notes: String = ""
)
