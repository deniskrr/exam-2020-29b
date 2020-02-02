package com.deniskrr.exam.ui

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.ui.core.EditorModel
import androidx.ui.core.Text
import androidx.ui.core.TextField
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.SolidColor
import androidx.ui.input.KeyboardType
import androidx.ui.layout.LayoutWidth
import androidx.ui.layout.Padding
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import androidx.ui.unit.dp

@Composable
fun ExamTextField(
    label: String,
    editorModel: MutableState<EditorModel>,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Row {
        Text(label, style = MaterialTheme.typography().body2)
        Spacer(modifier = LayoutWidth(8.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            borderWidth = 1.dp,
            borderBrush = SolidColor(Color.LightGray)
        ) {
            Padding(padding = 4.dp) {
                TextField(
                    value = editorModel.value, onValueChange = { newText ->
                        editorModel.value = newText
                    }, keyboardType = keyboardType,
                    textStyle = MaterialTheme.typography().body1
                )
            }
        }

    }
}