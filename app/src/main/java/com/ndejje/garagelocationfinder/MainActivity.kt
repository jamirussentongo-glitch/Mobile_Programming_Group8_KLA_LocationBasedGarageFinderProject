package com.ndejje.garagelocationfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.ndejje.garagelocationfinder.ui.navigation.Screen
import com.ndejje.garagelocationfinder.ui.screens.*
import com.ndejje.garagelocationfinder.ui.theme.GarageLocationFinderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GarageLocationFinderTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Bookings", Screen.Bookings.route, Icons.Default.DateRange),
        BottomNavItem("Profile", Screen.Profile.route, Icons.Default.Person)
    )

    val showBottomBar = bottomNavItems.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) { 
                LoginScreen(
                    onLoginSuccess = { navController.navigate(Screen.Home.route) }, 
                    onSignupClick = { navController.navigate(Screen.Signup.route) }
                ) 
            }
            composable(Screen.Signup.route) { 
                SignupScreen(
                    onSignupSuccess = { navController.navigate(Screen.Home.route) }, 
                    onLoginClick = { navController.popBackStack() }
                ) 
            }
            composable(Screen.Home.route) { 
                HomeScreen(onGarageClick = { id -> navController.navigate(Screen.GarageDetail.createRoute(id)) }) 
            }
            composable(Screen.Bookings.route) { 
                BookingsScreen() 
            }
            composable(Screen.Profile.route) { 
                ProfileScreen(onLogout = { navController.navigate(Screen.Login.route) { popUpTo(0) } }) 
            }
            composable(Screen.GarageDetail.route) { backStackEntry ->
                val garageId = backStackEntry.arguments?.getString("garageId") ?: ""
                GarageDetailScreen(garageId = garageId, onBack = { navController.popBackStack() })
            }
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: ImageVector)
