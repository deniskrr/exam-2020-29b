package com.deniskrr.exam.ui.my_section

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.EditorModel
import androidx.ui.core.Text
import androidx.ui.graphics.Color
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.dp
import com.deniskrr.exam.model.Request
import com.deniskrr.exam.ui.AppState
import com.deniskrr.exam.ui.ExamTextField

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
        RequestRecorder(appState)
        Spacer(modifier = LayoutHeight(16.dp))
        RequestList(appState)
    }
}

@Composable
fun RequestRecorder(appState: AppState) {
    val nameEditorModel = state { EditorModel() }
    val statusEditorModel = state { EditorModel() }
    val eCostEditorModel = state { EditorModel() }
    val costEditorModel = state { EditorModel() }
    Column {
        ExamTextField(label = "Name", editorModel = nameEditorModel)
        Spacer(modifier = LayoutHeight(8.dp))
        ExamTextField(label = "Status", editorModel = statusEditorModel)
        Spacer(modifier = LayoutHeight(8.dp))
        ExamTextField(
            label = "eCost",
            editorModel = eCostEditorModel,
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = LayoutHeight(8.dp))
        ExamTextField(
            label = "Cost",
            editorModel = costEditorModel,
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = LayoutHeight(16.dp))

        Button(text = "Send request", onClick = {
            val request = Request(
                nameEditorModel.value.text,
                statusEditorModel.value.text,
                appState.studentName,
                eCostEditorModel.value.text.toInt(),
                costEditorModel.value.text.toInt()
            )

            appState.recordRequest(request)
        })
    }
}

@Composable
fun RequestList(appState: AppState) {
    Column {
        Button(text = "Refresh list", onClick = {
            appState.getRequestsOfStudent(appState.studentName)
        })
        appState.requests.forEach { request ->
            RequestRow(request = request)
        }
    }
}

@Composable
fun RequestRow(request: Request) {
    Column {
        Padding(8.dp) {
            Column {
                Text(
                    "${request.name} initiated by ${request.student} is ${request.status}",
                    style = MaterialTheme.typography().body1.copy(Color.DarkGray)
                )
                Text(
                    text = "${request.cost} (${request.eCost}",
                    style = MaterialTheme.typography().caption
                )
            }
        }
        Divider(color = Color.LightGray, height = 2.dp)
    }
}

@Composable
fun StudentNameForm(appState: AppState) {
    val studentName = appState.studentName
    val studentNameEditor = state { EditorModel(studentName) }
    Column {
        ExamTextField(label = "Student name", editorModel = studentNameEditor)

        Spacer(modifier = LayoutHeight(8.dp))

        Button(text = "Save name", onClick = {
            appState.studentName = studentNameEditor.value.text
        })
    }
}