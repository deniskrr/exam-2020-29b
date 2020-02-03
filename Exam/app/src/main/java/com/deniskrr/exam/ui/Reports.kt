package com.deniskrr.exam.ui

import android.util.Log
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.frames.ModelList
import androidx.compose.frames.modelListOf
import androidx.compose.remember
import androidx.ui.layout.Center
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.EdgeInsets
import androidx.ui.material.Button
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.unit.dp
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
}

@Composable
fun ReportsContent(reportsState: ReportsState) {
    Column {
        Button(text = "Show filled requests", onClick = {
            reportsState.getFilledRequestDescendingByCost()
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
                    Log.d(TAG, "Failed retrieving filled requests sorted by cost")
                }
                isLoading = false
            }
        })
    }
}