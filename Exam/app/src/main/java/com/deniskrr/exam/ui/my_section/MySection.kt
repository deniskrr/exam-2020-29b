package com.deniskrr.exam.ui.my_section

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.EditorModel
import androidx.ui.core.Text
import androidx.ui.core.TextField
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.unit.dp
import com.deniskrr.exam.ui.AppState

@Composable
fun MySectionScreen(appState: AppState) {
    Container(padding = EdgeInsets(16.dp)) {
        MySectionContent(appState)
    }
}

@Composable
fun MySectionContent(appState: AppState) {
    Column {
        StudentNameForm(appState)
        Spacer(modifier = LayoutHeight(16.dp))
        RequestList(appState)
    }
}

@Composable
fun RequestList(appState: AppState) {
    Column {
        Button(text = "Refresh list", onClick = {
            appState.getRequestsOfStudent(appState.studentName)
        })
        appState.requests.forEach {
            Text(text = it.student)
        }
    }
}

@Composable
fun StudentNameForm(appState: AppState) {
    val studentName = appState.studentName
    val state = state { EditorModel(studentName) }
    Column {
        TextField(value = state.value, onValueChange = { newName ->
            state.value = newName
        })

        Spacer(modifier = LayoutHeight(8.dp))

        Button(text = "Save name", onClick = {
            appState.studentName = state.value.text
        })
    }
}