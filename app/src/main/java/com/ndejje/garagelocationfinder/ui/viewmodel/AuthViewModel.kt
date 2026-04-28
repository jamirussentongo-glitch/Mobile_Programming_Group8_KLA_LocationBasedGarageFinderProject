package com.ndejje.garagelocationfinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.garagelocationfinder.data.model.User
import com.ndejje.garagelocationfinder.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    val currentUser: StateFlow<User?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = repository.login(email, password)
            if (user != null) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Invalid email or password")
            }
        }
    }

    fun signup(name: String, email: String, password: String, phoneNumber: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val newUser = User(email, name, password, phoneNumber)
            repository.register(newUser)
            _authState.value = AuthState.Success
        }
    }

    fun updateProfile(name: String, phoneNumber: String, profileImageUri: String?) {
        viewModelScope.launch {
            val current = currentUser.value
            if (current != null) {
                val updatedUser = current.copy(
                    name = name,
                    phoneNumber = phoneNumber,
                    profileImageUri = profileImageUri
                )
                repository.updateUser(updatedUser)
            }
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        if (_authState.value !is AuthState.Success) {
            _authState.value = AuthState.Idle
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
