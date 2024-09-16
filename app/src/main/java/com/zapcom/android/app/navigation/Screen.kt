package com.zapcom.android.app.navigation

/**
 * Screen
 *
 * @property route
 * @constructor Create empty Screen
 */
sealed class Screen(val route: String) {
    data object Section : Screen("section")
}
