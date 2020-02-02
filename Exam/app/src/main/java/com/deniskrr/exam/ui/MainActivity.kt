package com.deniskrr.exam.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Context
import androidx.compose.Model
import androidx.compose.frames.ModelList
import androidx.compose.frames.modelListOf
import androidx.ui.animation.Crossfade
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import com.deniskrr.exam.model.Request
import com.deniskrr.exam.repository.Repository
import com.deniskrr.exam.repository.remote.RemoteRepository
import com.deniskrr.exam.ui.my_section.MySectionScreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    companion object {
        const val SHARED_PREFERENCES_NAME = "ExamPrefs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val appState = AppState(RemoteRepository(), sharedPreferences)

        setContent {
            MaterialTheme {
                AppContent(appState)
            }
        }
    }

}

@Model
class AppState(
    private val repository: Repository,
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val TAG = "AppState"
        const val STUDENT_PREFS_KEY = "Student"
    }

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


@Composable
fun AppContent(appState: AppState) {
    Crossfade(Navigator.currentScreen) { screen ->
        when (screen) {
            is Screen.MySection -> MySectionScreen(appState)
        }
    }
}


