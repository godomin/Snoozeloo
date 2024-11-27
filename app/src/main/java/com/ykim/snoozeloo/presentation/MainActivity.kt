package com.ykim.snoozeloo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.ykim.snoozeloo.presentation.navigation.NavigationRoot
import com.ykim.snoozeloo.presentation.list.ListViewModel
import com.ykim.snoozeloo.ui.theme.SnoozelooTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val listViewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                listViewModel.state.isLoading
            }
        }
        enableEdgeToEdge()
        setContent {
            SnoozelooTheme {
                val navController = rememberNavController()
                NavigationRoot(navController = navController)
            }
        }
    }
}