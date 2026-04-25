package com.ndejje.garagelocationfinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.garagelocationfinder.data.model.User
import com.ndejje.garagelocationfinder.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

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

    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.register(User(email, name, password))
            _authState.value = AuthState.Success
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
