package com.example.wtc_chat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.wtc_chat.ui.AppNavigation
import com.example.wtc_chat.ui.theme.WtcChatTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WtcChatTheme {
                AppNavigation()
            }
        }
    }
}
