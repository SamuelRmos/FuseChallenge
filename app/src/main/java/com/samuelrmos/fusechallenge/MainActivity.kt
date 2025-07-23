package com.samuelrmos.fusechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.samuelrmos.fusechallenge.navigation.Actions
import com.samuelrmos.fusechallenge.navigation.NavGraph
import com.samuelrmos.fusechallenge.ui.theme.FusechallengeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashscreen = installSplashScreen()
        var keepSplashScreen = true
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch {
            delay(3000)
            keepSplashScreen = false
        }
        setContent {
            FusechallengeTheme {
                val navController = rememberNavController()
                val actions = remember(navController) { Actions(navController) }
                NavGraph(navController, actions)
            }
        }
    }
}