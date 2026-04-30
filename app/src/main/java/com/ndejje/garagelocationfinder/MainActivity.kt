package com.ndejje.garagelocationfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    item.icon, 
                                    contentDescription = item.label,
                                    tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
                                ) 
                            },
                            label = { 
                                Text(
                                    item.label,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
                                ) 
                            },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            )
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
                    onLoginSuccess = { navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    } }, 
                    onSignupClick = { navController.navigate(Screen.Signup.route) }
                ) 
            }
            composable(Screen.Signup.route) { 
                SignupScreen(
                    onSignupSuccess = { navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                        popUpTo(Screen.Login.route) { inclusive = true }
                    } }, 
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
                ProfileScreen(onLogout = { navController.navigate(Screen.Login.route) { 
                    popUpTo(0) { inclusive = true }
                } })
            }
            composable(Screen.GarageDetail.route) { backStackEntry ->
                val garageId = backStackEntry.arguments?.getString("garageId") ?: ""
                GarageDetailScreen(garageId = garageId, onBack = { navController.popBackStack() })
            }
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: ImageVector)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainAppPreview() {
    GarageLocationFinderTheme {
        MainApp()
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    GarageLocationFinderTheme {
        LoginScreen(onLoginSuccess = {}, onSignupClick = {})
    }
}
