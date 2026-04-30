package com.ndejje.garagelocationfinder.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.ndejje.garagelocationfinder.data.model.Garage
import com.ndejje.garagelocationfinder.ui.viewmodel.GarageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGarageClick: (String) -> Unit,
    viewModel: GarageViewModel = hiltViewModel()
) {
    val garages by viewModel.garages.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isMapView by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Garage Finder") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isMapView = !isMapView },
                icon = {
                    Icon(
                        if (isMapView) Icons.Default.List else Icons.Default.LocationOn,
                        contentDescription = null
                    )
                },
                text = { Text(if (isMapView) "Show List" else "Show Map") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search services or garages...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            val filteredGarages = if (searchQuery.isEmpty()) {
                garages
            } else {
                garages.filter { garage ->
                    garage.services.any { it.contains(searchQuery, ignoreCase = true) } ||
                            garage.name.contains(searchQuery, ignoreCase = true)
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                if (isMapView) {
                    MapContent(filteredGarages, onGarageClick, viewModel)
                } else {
                    ListContent(filteredGarages, onGarageClick)
                }
            }
        }
    }
}

@Composable
fun ListContent(garages: List<Garage>, onGarageClick: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(garages) { garage ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onGarageClick(garage.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = garage.name, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                        garage.distance?.let {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                    Text(text = garage.address, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Services: ${garage.services.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "⭐ ${garage.rating}", style = MaterialTheme.typography.labelLarge)
                        Text(text = "View Details", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun MapContent(
    garages: List<Garage>,
    onGarageClick: (String) -> Unit,
    viewModel: GarageViewModel
) {
    val kampala = LatLng(0.3476, 32.5825)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(kampala, 12f)
    }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = true,
                mapType = MapType.NORMAL
            )
        )
    }

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = true,
                mapToolbarEnabled = true
            )
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings,
        onMyLocationClick = { location ->
            viewModel.setUserLocation(LatLng(location.latitude, location.longitude))
        },
        onMyLocationButtonClick = {
            // Camera will automatically move to my location, but we can also update our state
            false
        }
    ) {
        garages.forEach { garage ->
            Marker(
                state = MarkerState(position = LatLng(garage.latitude, garage.longitude)),
                title = garage.name,
                snippet = "${garage.distance ?: ""} | ${garage.address}",
                onInfoWindowClick = {
                    onGarageClick(garage.id)
                }
            )
        }
    }
}
