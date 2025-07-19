package com.samuelrmos.fusechallenge.navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samuelrmos.fusechallenge.navigation.Screens.DetailScreen
import com.samuelrmos.fusechallenge.navigation.Screens.ListMatchScreen
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
//            val coinExchange = it.requiredArg<MatchesResponse>(DetailScreen.route)
//            DetailScreen(coinExchange, actions)
        }
    }
}

inline fun <reified T : Parcelable> NavBackStackEntry.requiredArg(key: String): T =
    requireNotNull(arguments) { "arguments bundle is null" }.run {
        requireNotNull(getParcelable(key, T::class.java)) { "argument for $key is null" }
    }