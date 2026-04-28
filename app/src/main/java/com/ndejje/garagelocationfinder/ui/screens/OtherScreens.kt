package com.ndejje.garagelocationfinder.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
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
    var isEditing by remember { mutableStateOf(false) }
    
    var editedName by remember(user) { mutableStateOf(user?.name ?: "") }
    var editedPhone by remember(user) { mutableStateOf(user?.phoneNumber ?: "") }
    var selectedImageUri by remember(user) { mutableStateOf(user?.profileImageUri?.let { Uri.parse(it) }) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri = it }
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                        }
                    } else {
                        IconButton(onClick = { 
                            viewModel.updateProfile(editedName, editedPhone, selectedImageUri?.toString())
                            isEditing = false 
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Save")
                        }
                    }
                }
            ) 
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable(enabled = isEditing) { photoPickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                user?.name?.take(1)?.uppercase() ?: "?", 
                                style = MaterialTheme.typography.displayLarge
                            )
                        }
                    }
                }
                if (isEditing) {
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd).size(32.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        tonalElevation = 4.dp
                    ) {
                        Icon(
                            Icons.Default.Add, 
                            contentDescription = null, 
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (isEditing) {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = editedPhone,
                    onValueChange = { editedPhone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { isEditing = false },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                ) {
                    Text("Cancel")
                }
            } else {
                ProfileInfoItem(icon = Icons.Default.Person, label = "Name", value = user?.name ?: "Guest")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileInfoItem(icon = Icons.Default.Email, label = "Email", value = user?.email ?: "Not logged in")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileInfoItem(icon = Icons.Default.Phone, label = "Phone", value = user?.phoneNumber ?: "Not provided")
                
                Spacer(modifier = Modifier.weight(1f))
                
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
}

@Composable
fun ProfileInfoItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
