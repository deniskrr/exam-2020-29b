package com.deniskrr.exam.ui

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Context
import androidx.ui.animation.Crossfade
import androidx.ui.core.setContent
import androidx.ui.layout.Column
import androidx.ui.material.MaterialTheme
import com.deniskrr.exam.lightThemeColors
import com.deniskrr.exam.themeTypography

class MainActivity : AppCompatActivity() {

    companion object {
        const val SHARED_PREFERENCES_NAME = "ExamPrefs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerNetworkCallBack()

        setContent {
            ExamApp()
        }
    }


    private fun registerNetworkCallBack() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {

        })
    }
}


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