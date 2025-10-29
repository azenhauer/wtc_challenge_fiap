package com.example.wtc_chat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wtc_chat.data.model.Client
import com.example.wtc_chat.ui.viewmodel.CrmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrmScreen(viewModel: CrmViewModel = viewModel(), onNavigateToNewCampaign: () -> Unit) {
    val clients by viewModel.clients.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToNewCampaign) {
                Icon(Icons.Default.Add, contentDescription = "Nova Campanha")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Buscar por nome, tag, pontuação ou status") },
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier.padding(8.dp)
            ) {
                items(clients) {
                    ClientListItem(client = it)
                }
            }
        }
    }
}

@Composable
fun ClientListItem(client: Client) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = client.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
