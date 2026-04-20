package com.ndejje.garagelocationfinder.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object Bookings : Screen("bookings")
    object Profile : Screen("profile")
    object GarageDetail : Screen("garage_detail/{garageId}") {
        fun createRoute(garageId: String) = "garage_detail/$garageId"
    }
}
