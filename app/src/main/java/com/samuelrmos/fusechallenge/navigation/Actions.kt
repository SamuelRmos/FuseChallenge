package com.samuelrmos.fusechallenge.navigation

import android.os.Parcelable
import androidx.navigation.NavHostController
import com.samuelrmos.fusechallenge.navigation.Screens.ListMatchScreen

class Actions(private val navHostController: NavHostController) {

    val goToDetailScreen: (Pair<String, Parcelable>) -> Unit = { args ->
        with(navHostController) {
            val (route, parcelable) = args
            navigate(route)
            requireNotNull(currentBackStackEntry?.arguments).apply {
                putParcelable(route, parcelable)
            }
        }
    }

    val goToListScreen: () -> Unit = {
        navHostController.navigate(ListMatchScreen.route)
    }
}