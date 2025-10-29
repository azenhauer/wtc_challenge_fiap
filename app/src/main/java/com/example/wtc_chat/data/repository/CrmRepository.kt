package com.example.wtc_chat.data.repository

import com.example.wtc_chat.data.model.Client
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CrmRepository {



    fun getClients(): Flow<List<Client>> = flow {
        val mockClients = listOf(
            Client(id = "1", name = "Alice Johnson", email = "alice.j@email.com", tags = listOf("new-lead", "priority"), score = 95, status = "Active"),
            Client(id = "2", name = "Bob Williams", email = "bob.w@email.com", tags = listOf("interested"), score = 80, status = "Active"),
            Client(id = "3", name = "Charlie Brown", email = "charlie.b@email.com", tags = listOf("inactive", "churn-risk"), score = 30, status = "Inactive"),
            Client(id = "4", name = "Diana Miller", email = "diana.m@email.com", tags = listOf("loyal-customer"), score = 100, status = "Active"),
            Client(id = "5", name = "Ethan Davis", email = "ethan.d@email.com", tags = listOf("new-lead"), score = 75, status = "Active"),
            Client(id = "6", name = "Fiona Garcia", email = "fiona.g@email.com", tags = listOf("interested"), score = 85, status = "Active"),
            Client(id = "7", name = "George Rodriguez", email = "george.r@email.com", tags = listOf("inactive"), score = 45, status = "Inactive"),
            Client(id = "8", name = "Hannah Martinez", email = "hannah.m@email.com", tags = listOf("loyal-customer", "priority"), score = 98, status = "Active"),
            Client(id = "9", name = "Ian Hernandez", email = "ian.h@email.com", tags = listOf("new-lead"), score = 70, status = "Active"),
            Client(id = "10", name = "Jane Lopez", email = "jane.l@email.com", tags = listOf("churn-risk"), score = 40, status = "Inactive")
        )
        emit(mockClients)

        /*

        val snapshot = firestore.collection("clients").get().await()
        val clients = snapshot.toObjects(Client::class.java)
        emit(clients)
        */
    }
}
