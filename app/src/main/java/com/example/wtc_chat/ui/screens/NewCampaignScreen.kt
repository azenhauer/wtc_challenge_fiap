package com.example.wtc_chat.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wtc_chat.ui.viewmodel.CampaignViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCampaignScreen(viewModel: CampaignViewModel = viewModel()) {
    val campaign by viewModel.campaignState.collectAsState()
    val sendState by viewModel.sendState.collectAsState()
    val segments by viewModel.segments.collectAsState()
    val clientCount by viewModel.clientCount.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = campaign.title,
            onValueChange = { viewModel.onTitleChange(it) },
            label = { Text("Título da Campanha") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = campaign.message,
            onValueChange = { viewModel.onMessageChange(it) },
            label = { Text("Mensagem") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                TextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = campaign.segment,
                    onValueChange = {},
                    label = { Text("Segmento") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    segments.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                viewModel.onSegmentChange(selectionOption)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("$clientCount clientes serão alvo")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.sendCampaign() },
            modifier = Modifier.fillMaxWidth(),
            enabled = campaign.title.isNotBlank() && campaign.message.isNotBlank() && campaign.segment.isNotBlank()
        ) {
            Text("Enviar Agora")
        }

        Spacer(modifier = Modifier.height(16.dp))

        sendState?.let { result ->
            val message = if (result.isSuccess) {
                "✅ Campanha enviada com sucesso!"
            } else {
                "❌ Falha ao enviar campanha: ${result.exceptionOrNull()?.message}"
            }
            Text(message)
        }
    }
}
