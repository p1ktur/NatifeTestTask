package com.task.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.task.app.ui.theme.DarkerBlue
import com.task.app.ui.theme.NatifeTestTaskTheme
import com.task.feature_gifs.ui.navigation.GifsNavRoutes
import com.task.feature_gifs.ui.navigation.addGifsRoutes

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(DarkerBlue.toArgb())
        )

        setContent {
            val navController = rememberNavController()

            NatifeTestTaskTheme {
                NavHost(
                    navController = navController,
                    startDestination = GifsNavRoutes.Search,
                    enterTransition = { EnterTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popExitTransition = { ExitTransition.None }
                ) {
                    addGifsRoutes(navController = navController)
                }
            }
        }
    }
}
