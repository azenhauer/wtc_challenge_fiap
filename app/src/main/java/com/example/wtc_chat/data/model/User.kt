package com.example.wtc_chat.data.model

enum class UserRole {
    OPERATOR,
    CLIENT
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole
)
