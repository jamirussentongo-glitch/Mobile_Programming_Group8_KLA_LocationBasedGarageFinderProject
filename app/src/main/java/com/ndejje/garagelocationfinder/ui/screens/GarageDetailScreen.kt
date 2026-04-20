package com.ndejje.garagelocationfinder.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ndejje.garagelocationfinder.data.model.Garage
import com.ndejje.garagelocationfinder.ui.viewmodel.GarageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GarageDetailScreen(
    garageId: String,
    onBack: () -> Unit,
    viewModel: GarageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var garage by remember { mutableStateOf<Garage?>(null) }
    
    LaunchedEffect(garageId) {
        garage = viewModel.getGarageById(garageId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(garage?.name ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        garage?.let { item ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(text = item.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Address: ${item.address}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Rating: ${item.rating} ⭐", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Contact: ${item.contact}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        val gmmIntentUri = Uri.parse("google.navigation:q=${item.latitude},${item.longitude}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        context.startActivity(mapIntent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Navigate to Garage")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Services Offered:", style = MaterialTheme.typography.titleMedium)
                item.services.forEach { service ->
                    Text(text = "• $service", modifier = Modifier.padding(start = 8.dp, top = 4.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { /* Handle booking request */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Book a Service")
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
