package com.zapcom.android.app.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * App navigation
 *
 */
@Composable
internal fun AppNavigation(
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Section.route) {
        composable(route = Screen.Section.route) {
            HomeScreen()
        }
    }

}