package com.example.wtc_chat.ui.components

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.wtc_chat.data.model.Message

@Composable
fun MessageCard(message: Message) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            message.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = message.body,
                style = MaterialTheme.typography.bodyMedium
            )
            if (message.actions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    message.actions.forEach { action ->
                        Button(
                            onClick = {
                                when (action.type) {
                                    "url" -> {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(action.value))
                                        context.startActivity(intent)
                                    }
                                    "deeplink" -> {
                                        Log.d("MessageCard", "Deeplink clicked: ${action.value}")

                                    }
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = action.text)
                        }
                    }
                }
            }
        }
    }
}
