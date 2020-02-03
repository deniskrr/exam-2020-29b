package com.deniskrr.exam.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.animation.Crossfade
import androidx.ui.core.setContent
import androidx.ui.layout.Column
import androidx.ui.material.MaterialTheme
import com.deniskrr.exam.lightThemeColors
import com.deniskrr.exam.repository.EchoWebSocketListener
import com.deniskrr.exam.themeTypography
import okhttp3.OkHttpClient
import okhttp3.Request


class MainActivity : AppCompatActivity() {

    companion object {
        const val SHARED_PREFERENCES_NAME = "ExamPrefs"
    }

    private fun showToast(toast: String?) {
        runOnUiThread {
            Toast.makeText(
                this,
                toast,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createSocket()

        setContent {
            ExamApp()
        }
    }

    private fun createSocket() {
        val client = OkHttpClient()
        val request: Request = Request.Builder().url("ws://10.0.2.2:2902").build()
        val listener = EchoWebSocketListener { text ->
            showToast(text)
        }
        client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()

        client.dispatcher.executorService.shutdown()
    }

}

//@Composable
//fun registerNetworkCallBack(networkState: NetworkState) {
//    val connectivityManager =
//        ambient(key = ContextAmbient).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    connectivityManager.registerDefaultNetworkCallback(object :
//        ConnectivityManager.NetworkCallback() {
//        override fun onLost(network: Network) {
//            networkState.isConnected = false
//        }
//
//        override fun onAvailable(network: Network) {
//            networkState.isConnected = true
//        }
//    })
//}


@Composable
fun ExamApp() {

    MaterialTheme(colors = lightThemeColors, typography = themeTypography) {
        Column {
            TabBar()
            AppContent()
        }
    }
}

@Composable
fun AppContent() {
    Crossfade(Navigator.currentScreen) { screen ->
        when (screen) {
            is Screen.MySection -> MySectionScreen()
            is Screen.AllSection -> AllSectionScreen()
            is Screen.Reports -> ReportsScreen()
        }
    }
}
