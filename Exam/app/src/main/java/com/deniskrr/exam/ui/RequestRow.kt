package com.deniskrr.exam.ui

import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.Clickable
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.layout.Padding
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.unit.dp
import com.deniskrr.exam.model.Request

@Composable
fun RequestRow(request: Request, onClick: () -> Unit = {}) {
    Column {
        Padding(8.dp) {
            Clickable(onClick = onClick) {
                Column {
                    Text(
                        "${request.name} (${request.id}) initiated by ${request.student} is ${request.status}",
                        style = MaterialTheme.typography().body1.copy(Color.DarkGray)
                    )
                    Text(
                        text = "${request.cost} (${request.eCost}",
                        style = MaterialTheme.typography().caption
                    )
                }
            }

        }
        Divider(color = Color.LightGray, height = 2.dp)
    }
}