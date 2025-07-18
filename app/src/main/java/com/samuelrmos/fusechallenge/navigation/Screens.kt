package com.samuelrmos.fusechallenge.navigation

sealed class Screens(val route: String) {
    data object ListMatchScreen: Screens("list_screen")
    data object DetailScreen: Screens("detail_screen")
}