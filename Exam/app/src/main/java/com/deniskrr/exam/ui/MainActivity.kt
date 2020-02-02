package com.deniskrr.exam.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Model
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import com.deniskrr.exam.model.Request
import com.deniskrr.exam.repository.Repository
import com.deniskrr.exam.ui.my_section.MySectionScreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MySectionScreen()
            }
        }
    }
}

@Model
class AppState(private val repository: Repository) {
    var currentScreen = Screen.MySection
    val requests: List<Request> = listOf()

    fun recordRequest(request: Request) {
        repository.recordRequest(request, object : Callback<Request> {
            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.e("Request", "Error", t)
            }

            override fun onResponse(call: Call<Request>, response: Response<Request>) {
                Log.d("Request", "Success")
                if (response.isSuccessful) {
                    val request = response.body()
                    Log.d("Request", request.toString())
                }
            }

        })
    }

}

