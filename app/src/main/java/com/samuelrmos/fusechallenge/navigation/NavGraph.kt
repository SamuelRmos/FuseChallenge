package com.samuelrmos.fusechallenge.navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samuelrmos.fusechallenge.data.MatchItem
import com.samuelrmos.fusechallenge.navigation.Screens.DetailScreen
import com.samuelrmos.fusechallenge.navigation.Screens.ListMatchScreen
import com.samuelrmos.fusechallenge.ui.details.DetailsScreen
import com.samuelrmos.fusechallenge.ui.list.ListMatchesViewModel
import com.samuelrmos.fusechallenge.ui.list.MatchesListScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    actions: Actions,
    viewModel: ListMatchesViewModel = koinViewModel()
) {
    NavHost(navController = navController, startDestination = ListMatchScreen.route) {
        composable(route = ListMatchScreen.route) {
            MatchesListScreen(actions = actions, viewModel = viewModel)
        }
        composable(route = DetailScreen.route) {
            val matchItem = it.requiredArg<MatchItem>(DetailScreen.route)
            DetailsScreen(matchItem, actions, it)
        }
    }
}

inline fun <reified T : Parcelable> NavBackStackEntry.requiredArg(key: String): T =
    requireNotNull(arguments) { "arguments bundle is null" }.run {
        requireNotNull(getParcelable(key, T::class.java)) { "argument for $key is null" }
    }