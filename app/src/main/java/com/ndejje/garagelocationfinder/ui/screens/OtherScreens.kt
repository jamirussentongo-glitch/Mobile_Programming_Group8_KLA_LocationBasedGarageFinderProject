package com.ndejje.garagelocationfinder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ndejje.garagelocationfinder.data.model.Booking
import com.ndejje.garagelocationfinder.ui.viewmodel.AuthViewModel
import com.ndejje.garagelocationfinder.ui.viewmodel.BookingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    viewModel: BookingViewModel = hiltViewModel()
) {
    val bookings by viewModel.bookings.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Bookings") }) }
    ) { padding ->
        if (bookings.isEmpty()) {
            Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No bookings yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(bookings) { booking ->
                    BookingItem(booking)
                }
            }
        }
    }
}

@Composable
fun BookingItem(booking: Booking) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = booking.garageName, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = booking.status,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Service: ${booking.service}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Date: ${booking.date}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Booked by: ${booking.userName}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val user by viewModel.currentUser.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    val initial = user?.name?.take(1)?.uppercase() ?: "?"
                    Text(initial, style = MaterialTheme.typography.headlineLarge)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(user?.name ?: "Guest", style = MaterialTheme.typography.headlineSmall)
            Text(user?.email ?: "Not logged in", style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout")
            }
        }
    }
}
