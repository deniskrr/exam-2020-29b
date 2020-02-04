package com.deniskrr.exam.ui

import android.util.Log
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.ambient
import androidx.compose.frames.ModelList
import androidx.compose.frames.modelListOf
import androidx.compose.remember
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Text
import androidx.ui.layout.Center
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.EdgeInsets
import androidx.ui.material.AlertDialog
import androidx.ui.material.Button
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.unit.dp
import com.deniskrr.exam.extensions.getErrorMessage
import com.deniskrr.exam.isConnected
import com.deniskrr.exam.model.Request
import com.deniskrr.exam.repository.Repository
import com.deniskrr.exam.repository.remote.RemoteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ReportsScreen() {
    val reportsState = remember { ReportsState(RemoteRepository()) }

    Container(padding = EdgeInsets(16.dp)) {
        ReportsContent(reportsState)
    }

    if (reportsState.isLoading) Center {
        CircularProgressIndicator()
    }

    if (reportsState.errorMessage.isNotBlank()) {
        AlertDialog(
            onCloseRequest = { reportsState.errorMessage = "" },
            title = { Text("Error!") },
            text = { Text(reportsState.errorMessage) },
            buttons = { Button(text = "Cool", onClick = { reportsState.errorMessage = "" }) }
        )
    }
}

@Composable
fun ReportsContent(reportsState: ReportsState) {
    Column {
        val context = ambient(key = ContextAmbient)
        Button(text = "Show filled requests", onClick = {
            if (isConnected(context)) {
                reportsState.getFilledRequestDescendingByCost()
            } else {
                reportsState.errorMessage = "You are not connected to Internet"
            }
        })
        RequestList(reportsState)
    }
}

@Composable
private fun RequestList(reportsState: ReportsState) {
    Column {
        reportsState.requests.forEach { request ->
            RequestRow(request = request)
        }
    }
}


@Model
class ReportsState(private val repository: Repository) {
    companion object {
        const val TAG = "Reports"
    }

    var errorMessage = ""
    var isLoading: Boolean = false
    val requests: ModelList<Request> = modelListOf()

    fun getFilledRequestDescendingByCost() {
        isLoading = true
        repository.getFilledRequestDescendingByCost(object : Callback<List<Request>> {
            override fun onFailure(call: Call<List<Request>>, t: Throwable) {
                isLoading = false
                Log.e(TAG, "Error retrieving filled requests sorted by cost", t)
            }

            override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
                if (response.isSuccessful) {
                    val responseRequests = response.body()!!

                    with(requests) {
                        clear()
                        addAll(responseRequests.sortedByDescending { request ->
                            request.cost
                        })
                    }

                    Log.d(
                        TAG,
                        "Successfully retrieved filled requests sorted by cost"
                    )
                } else {
                    val responseErrorMessage = response.getErrorMessage()
                    errorMessage = responseErrorMessage
                    Log.d(
                        TAG,
                        "Failed retrieving filled requests sorted by cost. Error: $responseErrorMessage"
                    )
                }
                isLoading = false
            }
        })
    }
}