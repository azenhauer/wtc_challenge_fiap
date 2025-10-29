package com.example.wtc_chat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wtc_chat.data.model.User
import com.example.wtc_chat.data.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class PasswordResetState {
    object Idle : PasswordResetState()
    object Loading : PasswordResetState()
    object Success : PasswordResetState()
    data class Error(val message: String) : PasswordResetState()
}

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _passwordResetState = MutableStateFlow<PasswordResetState>(PasswordResetState.Idle)
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _selectedRole = MutableStateFlow(UserRole.CLIENT)
    val selectedRole: StateFlow<UserRole> = _selectedRole

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _currentUser.value = firebaseAuth.currentUser
    }

    init {
        auth.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }

    fun onRoleSelected(role: UserRole) {
        _selectedRole.value = role
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user
                if (firebaseUser != null) {
                    val user = User(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        role = _selectedRole.value
                    )
                    _loginState.value = LoginState.Success(user)
                } else {
                    _loginState.value = LoginState.Error("User not found")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user
                if (firebaseUser != null) {
                    val user = User(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        role = _selectedRole.value
                    )
                    _loginState.value = LoginState.Success(user)
                } else {
                    _loginState.value = LoginState.Error("User not found")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun passwordReset(email: String) {
        if (email.isBlank()) {
            _passwordResetState.value = PasswordResetState.Error("Email cannot be empty.")
            return
        }
        viewModelScope.launch {
            _passwordResetState.value = PasswordResetState.Loading
            try {
                auth.sendPasswordResetEmail(email).await()
                _passwordResetState.value = PasswordResetState.Success
            } catch (e: Exception) {
                _passwordResetState.value = PasswordResetState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetPasswordState() {
        _passwordResetState.value = PasswordResetState.Idle
    }

    fun logout() {
        auth.signOut()
        _loginState.value = LoginState.Idle
    }
}
