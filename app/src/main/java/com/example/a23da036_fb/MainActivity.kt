package com.example.a23da036_fb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.a23da036_fb.ui.theme._23DA036_FBTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _23DA036_FBTheme {
                FirebaseDemoScreen()
            }
        }
    }
}

@Composable
fun FirebaseDemoScreen() {
    val database = Firebase.database
    val myRef = database.getReference("message")

    var inputText by remember { mutableStateOf("") }
    var firebaseMessage by remember { mutableStateOf("Chưa có dữ liệu") }

    // Theo dõi dữ liệu từ Firebase
    LaunchedEffect(Unit) {
        myRef.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                firebaseMessage = snapshot.value?.toString() ?: "Trống"
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                firebaseMessage = "Lỗi: ${error.message}"
            }
        })
    }

    // Giao diện
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Kết nối Firebase Realtime Database", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Nhập nội dung để gửi") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            myRef.setValue(inputText)
        }) {
            Text("Gửi lên Firebase")
        }

        Spacer(Modifier.height(32.dp))

        Text("Dữ liệu từ Firebase:")
        Text(firebaseMessage)
    }
}
