package com.deniskrr.exam.ui

import android.util.Log
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.frames.ModelList
import androidx.compose.frames.modelListOf
import androidx.compose.remember
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.layout.EdgeInsets
import androidx.ui.material.Button
import androidx.ui.unit.dp
import com.deniskrr.exam.model.Request
import com.deniskrr.exam.repository.Repository
import com.deniskrr.exam.repository.remote.RemoteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun AllSectionScreen() {

    val allSectionState = remember { AllSectionState(RemoteRepository()) }

    Container(padding = EdgeInsets(16.dp)) {
        AllSectionContent(allSectionState)
    }
}

@Composable
fun AllSectionContent(allSectionState: AllSectionState) {
    Column {
        Button(text = "See all requests", onClick = {
            allSectionState.getOpenRequests()
        })
        RequestList(allSectionState = allSectionState)
    }
}

@Composable
private fun RequestList(allSectionState: AllSectionState) {
    VerticalScroller {
        Column {
            allSectionState.requests.forEach { request ->
                RequestRow(request = request)
            }
        }
    }

}

@Model
class AllSectionState(private val repository: Repository) {
    val requests: ModelList<Request> = modelListOf()

    fun getOpenRequests() {
        repository.getOpenRequests(object : Callback<List<Request>> {
            override fun onFailure(call: Call<List<Request>>, t: Throwable) {
                Log.e(MySectionState.TAG, "Error getting all open requests", t)
            }

            override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
                if (response.isSuccessful) {
                    val responseRequests = response.body()!!

                    with(requests) {
                        clear()
                        addAll(responseRequests)
                    }

                    Log.d(MySectionState.TAG, "Successfully retrieved all open requests")
                } else {
                    Log.d(MySectionState.TAG, "Failed retrieving all open requests")
                }
            }
        })
    }
}