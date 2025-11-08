package com.example.a23da036_fb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.a23da036_fb.ui.theme._23DA036_FBTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _23DA036_FBTheme {
                NoteAppScreen() // chạy app note
            }
        }
    }
}
