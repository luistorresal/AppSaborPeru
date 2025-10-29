package com.example.appsaborperu.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.appsaborperu.data.local.db.AppDatabase
import com.example.appsaborperu.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado del login.
 * - Inicializa la base de datos Room.
 * - Carga usuarios por defecto (3 iniciales).
 * - Valida las credenciales al iniciar sesión.
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // Instancia local de la base de datos y repositorio
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "saborperu.db"
    ).build()

    private val userRepo = UserRepository(db.userDao())

    // Estados observables para la UI
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    init {
        // Cargar los 3 usuarios iniciales al arrancar
        viewModelScope.launch {
            userRepo.seedIfEmpty()
        }
    }

    /**
     * Valida credenciales simples (email + password)
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepo.login(email, password)
            _loginState.value = if (user != null) {
                LoginState.Success(user.displayName)
            } else {
                LoginState.Error("Credenciales incorrectas")
            }
        }
    }

    /**
     * Ingresar como invitado (sin validación)
     */
    fun loginAsGuest() {
        _loginState.value = LoginState.Success("Invitado")
    }
}

/**
 * Representa los estados de la pantalla de login.
 */
sealed class LoginState {
    object Idle : LoginState()
    data class Success(val userName: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
