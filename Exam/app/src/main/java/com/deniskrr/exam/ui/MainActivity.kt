package com.deniskrr.exam.ui

import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Context
import androidx.compose.Model
import androidx.compose.frames.ModelList
import androidx.compose.frames.modelListOf
import androidx.compose.state
import androidx.ui.animation.Crossfade
import androidx.ui.core.Text
import androidx.ui.core.setContent
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.Column
import androidx.ui.layout.LayoutHeight
import androidx.ui.layout.Padding
import androidx.ui.layout.Spacer
import androidx.ui.material.*
import androidx.ui.material.surface.Surface
import androidx.ui.unit.dp
import com.deniskrr.exam.lightThemeColors
import com.deniskrr.exam.model.Request
import com.deniskrr.exam.repository.Repository
import com.deniskrr.exam.repository.remote.RemoteRepository
import com.deniskrr.exam.themeTypography
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

        registerNetworkCallBack(appState)

        setContent {
            ExamApp(appState)
        }
    }


    private fun registerNetworkCallBack(appState: AppState) {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                appState.isConnected = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                appState.isConnected = false
            }
        })
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


@Composable
fun ExamApp(appState: AppState) {
    val (drawerState, onDrawerStateChange) = state { DrawerState.Closed }

    MaterialTheme(colors = lightThemeColors, typography = themeTypography) {
        ModalDrawerLayout(
            drawerState = drawerState,
            onStateChange = onDrawerStateChange,
            gesturesEnabled = drawerState == DrawerState.Opened,
            drawerContent = {
                AppDrawer(
                    currentScreen = Navigator.currentScreen,
                    closeDrawer = { onDrawerStateChange(DrawerState.Closed) }
                )
            },
            bodyContent = { AppContent(appState) { onDrawerStateChange(DrawerState.Opened) } }
        )
    }
}


@Composable
fun AppContent(appState: AppState, openDrawer: () -> Unit) {
    Crossfade(Navigator.currentScreen) { screen ->
        when (screen) {
            is Screen.MySection -> MySectionScreen(appState) { openDrawer() }
        }
    }
}

@Composable
private fun AppDrawer(
    currentScreen: Screen,
    closeDrawer: () -> Unit
) {
    Column {
        Spacer(modifier = LayoutHeight(24.dp))
        DrawerButton(
            label = "My Section",
            isSelected = currentScreen == Screen.MySection
        ) {
            navigateTo(Screen.MySection)
            closeDrawer()
        }

        DrawerButton(
            label = "All Section",
            isSelected = currentScreen == Screen.AllSection
        ) {
            navigateTo(Screen.AllSection)
            closeDrawer()
        }
    }
}

@Composable
private fun DrawerButton(
    label: String,
    isSelected: Boolean,
    action: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colors().primary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colors().surface
    }

    Padding(left = 8.dp, top = 8.dp, right = 8.dp) {
        Surface(
            color = backgroundColor,
            shape = RoundedCornerShape(4.dp)
        ) {
            Button(onClick = action, style = TextButtonStyle()) {
                Text(
                    text = label
                )
            }
        }
    }
}


