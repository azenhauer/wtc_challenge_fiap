package com.example.wtc_chat.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wtc_chat.data.model.UserRole
import com.example.wtc_chat.ui.viewmodel.LoginState
import com.example.wtc_chat.ui.viewmodel.LoginViewModel
import com.example.wtc_chat.ui.viewmodel.PasswordResetState
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (UserRole) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val loginState by loginViewModel.loginState.collectAsState()
    val passwordResetState by loginViewModel.passwordResetState.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val selectedRole by loginViewModel.selectedRole.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(loginState, passwordResetState) {
        val message = when (val state = loginState) {
            is LoginState.Error -> "Error: ${state.message}"
            else -> null
        } ?: when (val state = passwordResetState) {
            is PasswordResetState.Success -> "Password reset email sent."
            is PasswordResetState.Error -> "Error: ${state.message}"
            else -> null
        }

        message?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            loginViewModel.resetPasswordState()
        }
    }

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess((loginState as LoginState.Success).user.role)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("WTC Chat", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            RoleSelector(
                selectedRole = selectedRole,
                onRoleSelected = { loginViewModel.onRoleSelected(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    loginViewModel.login(email, password)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is LoginState.Loading
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Text("Login")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(
                    onClick = onNavigateToSignUp,
                    enabled = loginState !is LoginState.Loading
                ) {
                    Text("Sign Up")
                }
                TextButton(
                    onClick = {
                        if (email.isNotBlank()) {
                            keyboardController?.hide()
                            loginViewModel.passwordReset(email)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please enter an email first.")
                            }
                        }
                    },
                    enabled = passwordResetState !is PasswordResetState.Loading
                ) {
                    Text("Forgot Password?")
                }
            }

            if (loginState is LoginState.Loading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun RoleSelector(
    selectedRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Role:")
        Spacer(modifier = Modifier.width(8.dp))
        RadioButton(
            selected = selectedRole == UserRole.CLIENT,
            onClick = { onRoleSelected(UserRole.CLIENT) }
        )
        Text("Client", Modifier.padding(start = 4.dp))
        Spacer(modifier = Modifier.width(16.dp))
        RadioButton(
            selected = selectedRole == UserRole.OPERATOR,
            onClick = { onRoleSelected(UserRole.OPERATOR) }
        )
        Text("Operator", Modifier.padding(start = 4.dp))
    }
}
