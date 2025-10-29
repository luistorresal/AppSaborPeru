package com.example.appsaborperu.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsaborperu.util.Validators
import com.example.appsaborperu.ui.auth.AuthViewModel
import com.example.appsaborperu.ui.auth.LoginState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff


/**
 * Pantalla de Login.
 *
 * @param onSuccess callback cuando el login es correcto (útil para navegar a Productos)
 * @param vm ViewModel de autenticación (por defecto lo resuelve Compose)
 */
@Composable
fun LoginScreen(
    onSuccess: (String) -> Unit,
    vm: AuthViewModel = viewModel()
) {
    val state by vm.loginState.collectAsStateWithLifecycle()

    // Estados locales de los inputs
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passError by remember { mutableStateOf<String?>(null) }

    // Reaccionar a éxito
    LaunchedEffect(state) {
        if (state is LoginState.Success) {
            onSuccess((state as LoginState.Success).userName)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SaborPeru",
            style = MaterialTheme.typography.headlineMedium
        )
