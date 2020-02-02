package com.deniskrr.exam.ui.my_section

import android.content.Context
import androidx.compose.Composable
import androidx.compose.ambient
import androidx.compose.state
import androidx.ui.core.ContextAmbient
import androidx.ui.core.EditorModel
import androidx.ui.core.Text
import androidx.ui.core.TextField
import androidx.ui.layout.Column
import androidx.ui.material.Button
import androidx.ui.material.TopAppBar

@Composable
fun MySectionScreen() {
    Column {
        TopAppBar(title = { Text("My Section") })
        MySectionContent()
    }
}

@Composable
fun MySectionContent() {
    StudentNameForm()
}

@Composable
fun StudentNameForm() {
    val studentNameKey = "StudentName"

    val context = ambient(key = ContextAmbient)
    val preferences = context.getSharedPreferences("Student", Context.MODE_PRIVATE)

    val studentName = preferences.getString(studentNameKey, "")!!
    val state = state { EditorModel(studentName) }
    Column {
        TextField(value = state.value, onValueChange = { newName ->
            state.value = newName
        })

        Button(text = "Save name", onClick = {
            preferences.edit().putString(studentNameKey, state.value.text).apply()
        })
    }
}