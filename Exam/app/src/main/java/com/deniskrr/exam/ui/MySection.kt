package com.deniskrr.exam.ui

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.*
import androidx.compose.frames.ModelList
import androidx.compose.frames.modelListOf
import androidx.ui.core.ContextAmbient
import androidx.ui.core.EditorModel
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.unit.dp
import com.deniskrr.exam.model.Request
import com.deniskrr.exam.repository.Repository
import com.deniskrr.exam.repository.remote.RemoteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MySectionScreen() {
    val sharedPreferences = ambient(key = ContextAmbient).getSharedPreferences(
        MainActivity.SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    val appState = remember { MySectionState(RemoteRepository(), sharedPreferences) }

    Container(padding = EdgeInsets(16.dp)) {
        MySectionContent(appState)
    }
}

@Composable
fun MySectionContent(mySectionState: MySectionState) {
    Column {
        StudentNameForm(mySectionState)
        Spacer(modifier = LayoutHeight(16.dp))
        RequestRecorder(mySectionState)
        Spacer(modifier = LayoutHeight(16.dp))
        RequestList(mySectionState)
    }
}

@Composable
fun RequestRecorder(mySectionState: MySectionState) {
    val nameEditorModel = state { EditorModel() }
    val statusEditorModel = state { EditorModel() }
    LayoutGravity
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
                0,
                nameEditorModel.value.text,
                statusEditorModel.value.text,
                mySectionState.studentName,
                eCostEditorModel.value.text.toInt(),
                costEditorModel.value.text.toInt()
            )

            mySectionState.recordRequest(request)
        })
    }
}

@Composable
private fun RequestList(mySectionState: MySectionState) {
    Column {
        Button(text = "Refresh list", onClick = {
            mySectionState.getRequestsOfStudent(mySectionState.studentName)
        })
        mySectionState.requests.forEach { request ->
            RequestRow(request = request)
        }
    }
}

@Composable
fun StudentNameForm(mySectionState: MySectionState) {
    val studentName = mySectionState.studentName
    val studentNameEditor = state { EditorModel(studentName) }
    Column {
        ExamTextField(label = "Student name", editorModel = studentNameEditor)

        Spacer(modifier = LayoutHeight(8.dp))

        Button(text = "Save name", onClick = {
            mySectionState.studentName = studentNameEditor.value.text
        })
    }
}


@Model
class MySectionState(
    private val repository: Repository,
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val TAG = "MySectionState"
        const val STUDENT_PREFS_KEY = "Student"
    }

    var isConnected = false

    var studentName: String
        get() = sharedPreferences.getString(STUDENT_PREFS_KEY, "")!!
        set(value) =
            sharedPreferences.edit().putString(STUDENT_PREFS_KEY, value).apply()

    val requests: ModelList<Request> = modelListOf()

    fun recordRequest(request: Request) {
        repository.recordRequest(request, object : Callback<Request> {
            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.e(TAG, "Error recording request", t)
            }

            override fun onResponse(call: Call<Request>, response: Response<Request>) {
                if (response.isSuccessful) {
                    val responseRequest = response.body()!!
                    Log.d(TAG, "Successfully recorded request ${responseRequest.name}")
                } else {
                    Log.d(TAG, "Failed recording request ${request.name}")
                }
            }
        })
    }

    fun getRequestsOfStudent(studentName: String) {
        repository.getRequestsOfStudent(studentName, object : Callback<List<Request>> {
            override fun onFailure(call: Call<List<Request>>, t: Throwable) {
                Log.e(TAG, "Error getting requests of student", t)
            }

            override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
                if (response.isSuccessful) {
                    val responseRequests = response.body()!!

                    with(requests) {
                        clear()
                        addAll(responseRequests)
                    }

                    Log.d(TAG, "Successfully retrieved requests of $studentName")
                } else {
                    Log.d(TAG, "Failed retrieving requests of $studentName")
                }
            }
        })
    }
}
