package com.example.wtc_chat.data.repository

import com.example.wtc_chat.data.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val collection = firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val registration = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val messages = snapshot.toObjects(Message::class.java)
                trySend(messages).isSuccess
            }
        }

        awaitClose { registration.remove() }
    }

    suspend fun sendMessage(chatId: String, message: Message) {
        firestore.collection("chats").document(chatId).collection("messages")
            .add(message)
            .await()
    }
}
