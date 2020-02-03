package com.deniskrr.exam.ui

import android.util.Log
import androidx.compose.Composable
import androidx.compose.Model
import androidx.compose.frames.ModelList
import androidx.compose.frames.modelListOf
import androidx.compose.remember
import androidx.compose.state
import androidx.ui.core.EditorModel
import androidx.ui.foundation.VerticalScroller
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
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
fun AllSectionScreen() {

    val allSectionState = remember { AllSectionState(RemoteRepository()) }

    Container(padding = EdgeInsets(16.dp)) {
        AllSectionContent(allSectionState)
    }

    if (allSectionState.isLoading) Center {
        CircularProgressIndicator()
    }
}

@Composable
private fun AllSectionContent(allSectionState: AllSectionState) {
    Column {
        val idEditorModel = state { EditorModel() }
        val costEditorModel = state { EditorModel() }
        val statusEditorModel = state { EditorModel() }
        ExamTextField(
            label = "Id",
            editorModel = idEditorModel,
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = LayoutHeight(8.dp))
        ExamTextField(
            label = "Cost",
            editorModel = costEditorModel,
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = LayoutHeight(8.dp))
        ExamTextField(label = "Status", editorModel = statusEditorModel)
        Spacer(modifier = LayoutHeight(8.dp))
        Button(text = "Change", onClick = {
            allSectionState.changeRequestStatus(
                Request(
                    id = idEditorModel.value.text.toInt(),
                    cost = costEditorModel.value.text.toInt(),
                    status = statusEditorModel.value.text
                )
            )
        })
        Spacer(modifier = LayoutHeight(16.dp))
        RequestList(allSectionState = allSectionState)
    }
}

@Composable
private fun RequestList(allSectionState: AllSectionState) {

    Column {
        Button(text = "See all requests", onClick = {
            allSectionState.getOpenRequests()
        })
        VerticalScroller {
            Column {
                allSectionState.requests.forEach { request ->
                    RequestRow(request = request)
                }
            }
        }
    }
}

@Model
class AllSectionState(private val repository: Repository) {
    companion object {
        const val TAG = "AllSectionState"
    }

    val requests: ModelList<Request> = modelListOf()
    var isLoading: Boolean = false

    fun getOpenRequests() {
        isLoading = true

        repository.getOpenRequests(object : Callback<List<Request>> {
            override fun onFailure(call: Call<List<Request>>, t: Throwable) {
                isLoading = false
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
                isLoading = false
            }
        })
    }

    fun changeRequestStatus(request: Request) {
        isLoading = true

        repository.changeRequestStatus(request, object : Callback<Request> {
            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.e(TAG, "Error changing request status", t)
                isLoading = false
            }

            override fun onResponse(call: Call<Request>, response: Response<Request>) {
                if (response.isSuccessful) {
                    val responseRequest = response.body()!!
                    Log.d(
                        MySectionState.TAG,
                        "Successfully changed request ${responseRequest.name} status to ${request.status}"
                    )
                } else {
                    Log.d(
                        MySectionState.TAG,
                        "Failed changing request ${request.name} status to ${request.status}"
                    )
                }
                isLoading = false
            }
        })
    }
}