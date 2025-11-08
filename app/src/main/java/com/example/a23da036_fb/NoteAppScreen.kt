package com.example.a23da036_fb

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun NoteAppScreen() {
    val database = Firebase.database.reference.child("notes")

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var notes by remember { mutableStateOf(listOf<Note>()) }
    var editingNote by remember { mutableStateOf<Note?>(null) }

    // Lắng nghe dữ liệu Realtime
    LaunchedEffect(Unit) {
        database.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val list = mutableListOf<Note>()
                for (child in snapshot.children) {
                    val note = child.getValue(Note::class.java)
                    note?.id = child.key
                    if (note != null) list.add(note)
                }
                notes = list
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                // Bạn có thể log lỗi ở đây nếu cần
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("QUẢN LÝ SẢN PHẨM", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        // Tên
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tên sản phẩm") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))

        // Loại / mô tả
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Loại / Mô tả") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // Giá
        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Giá") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        // Thêm / Cập nhật
        Button(
            onClick = {
                // Validate tối thiểu tên (bạn có thể thêm validate khác)
                if (title.isBlank()) return@Button

                val id = editingNote?.id ?: database.push().key ?: return@Button
                val note = Note(
                    id = id,
                    title = title.trim(),
                    description = description.trim(),
                    price = price.trim()
                )

                database.child(id).setValue(note)

                // Reset form
                title = ""
                description = ""
                price = ""
                editingNote = null
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editingNote == null) "Thêm sản phẩm" else "Cập nhật sản phẩm")
        }

        Spacer(Modifier.height(20.dp))

        Text("Danh sách sản phẩm:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        // Danh sách
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Tên: ${note.title}", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text("Loại: ${note.description}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(4.dp))
                        Text("Giá: ${note.price}", style = MaterialTheme.typography.bodyMedium)

                        Spacer(Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = {
                                // Điền form để sửa
                                title = note.title
                                description = note.description
                                price = note.price
                                editingNote = note
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Sửa")
                            }

                            IconButton(onClick = {
                                // Xóa
                                note.id?.let { database.child(it).removeValue() }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Xóa")
                            }
                        }
                    }
                }
            }
        }
    }
}
