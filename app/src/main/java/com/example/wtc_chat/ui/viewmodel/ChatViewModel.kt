package com.example.wtc_chat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wtc_chat.data.model.Message
import com.example.wtc_chat.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _latestMessage = MutableStateFlow<Message?>(null)
    val latestMessage: StateFlow<Message?> = _latestMessage.asStateFlow()


    private val chatId = "general"

    init {
        fetchMessages()
    }

    private fun fetchMessages() {
        viewModelScope.launch {
            repository.getMessages(chatId)
                .catch { e ->

                }
                .collect { messageList ->
                    if (_messages.value.isNotEmpty() && messageList.size > _messages.value.size) {
                        val newMessage = messageList.first()
                        if (newMessage.sender != "User") {
                           _latestMessage.value = newMessage
                        }
                    }
                    _messages.value = messageList
                }
        }
    }

    fun sendMessage(text: String, sender: String) {
        viewModelScope.launch {
            if (text.isNotBlank()) {
                val message = Message(
                    id = "",
                    title = null,
                    body = text,
                    sender = sender,
                    timestamp = System.currentTimeMillis()
                )
                repository.sendMessage(chatId, message)
            }
        }
    }

    fun dismissLatestMessage() {
        _latestMessage.value = null
    }
}
