package com.ndejje.garagelocationfinder.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var selectedBookingForRebook by remember { mutableStateOf<Booking?>(null) }

    if (selectedBookingForRebook != null) {
        RebookDialog(
            booking = selectedBookingForRebook!!,
            onDismiss = { selectedBookingForRebook = null },
            onConfirm = { newService ->
                viewModel.bookGarage(
                    garageId = selectedBookingForRebook!!.garageId,
                    garageName = selectedBookingForRebook!!.garageName,
                    userName = selectedBookingForRebook!!.userName,
                    service = newService
                )
                selectedBookingForRebook = null
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Bookings",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = Color(0xFFF7F9FC) // Distinct light background for the whole screen
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Header summary
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Booking History",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${bookings.size} total services",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            if (bookings.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No bookings yet", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp, top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(bookings) { booking ->
                        BookingItem(
                            booking = booking,
                            onRebookClick = { selectedBookingForRebook = booking }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookingItem(
    booking: Booking,
    onRebookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // White cards to pop against gray background
        ),
        border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = booking.garageName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Surface(
                    color = when(booking.status.lowercase()) {
                        "completed" -> Color(0xFFE8F5E9)
                        "pending" -> Color(0xFFFFF3E0)
                        else -> Color(0xFFF5F5F5)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = booking.status.uppercase(),
                        color = when(booking.status.lowercase()) {
                            "completed" -> Color(0xFF2E7D32)
                            "pending" -> Color(0xFFEF6C00)
                            else -> Color.DarkGray
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))
            
            // Enhanced Service Row with better visibility
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = booking.service,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = booking.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "REBOOK SERVICE →",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onRebookClick() }
                )
            }
        }
    }
}

@Composable
fun RebookDialog(
    booking: Booking,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var service by remember { mutableStateOf(booking.service) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rebook Service") },
        text = {
            Column {
                Text("Garage: ${booking.garageName}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = service,
                    onValueChange = { service = it },
                    label = { Text("Service Type") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Oil Change, Engine repair") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(service) }) {
                Text("Order Now")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
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
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Profile",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = Color.White)
                        }
                    } else {
                        IconButton(onClick = { 
                            viewModel.updateProfile(editedName, editedPhone, selectedImageUri?.toString())
                            isEditing = false 
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White)
                        }
                    }
                }
            ) 
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        val profileFieldColors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header Background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )

            // Overlapping Profile Picture
            Box(
                modifier = Modifier
                    .offset(y = (-50).dp)
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(4.dp)
                    .clip(CircleShape)
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
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                user?.name?.take(1)?.uppercase() ?: "?", 
                                style = MaterialTheme.typography.displayLarge,
                                color = Color.White
                            )
                        }
                    }
                }
                if (isEditing) {
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd).size(32.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondary,
                        tonalElevation = 4.dp
                    ) {
                        Icon(
                            Icons.Default.Add, 
                            contentDescription = null, 
                            tint = Color.White,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
            
            Column(
                modifier = Modifier
                    .offset(y = (-30).dp)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                if (isEditing) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = profileFieldColors
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = editedPhone,
                        onValueChange = { editedPhone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = profileFieldColors
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { isEditing = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                } else {
                    OutlinedTextField(
                        value = user?.name ?: "Guest",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = profileFieldColors
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = user?.email ?: "Not logged in",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = profileFieldColors
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = user?.phoneNumber ?: "Not provided",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = profileFieldColors
                    )
                    
                    Spacer(modifier = Modifier.height(40.dp))
                    
                    Button(
                        onClick = {
                            viewModel.logout()
                            onLogout()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logout", color = Color.White)
                    }
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
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            shape = CircleShape,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}
