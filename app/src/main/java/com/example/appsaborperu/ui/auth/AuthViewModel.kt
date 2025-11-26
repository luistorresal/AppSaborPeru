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
 * Solo permite acceso a los 3 usuarios autorizados:
 * - marines@saborperu.cl (admin)
 * - claudio@saborperu.cl (admin)
 * - luis@saborperu.cl (cliente)
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "saborperu.db"
    ).fallbackToDestructiveMigration()  // Recrear BD si hay cambios
    .build()

    private val userRepo = UserRepository(db.userDao())

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    init {
        // Cargar los 3 usuarios iniciales al arrancar
        viewModelScope.launch {
            userRepo.seedIfEmpty()
        }
    }

    /**
     * Valida credenciales. Solo los 3 usuarios autorizados pueden acceder.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            // Validar formato de email
            if (email.isBlank() || password.isBlank()) {
                _loginState.value = LoginState.Error("Complete todos los campos")
                return@launch
            }
            
            val user = userRepo.login(email.trim().lowercase(), password)
            _loginState.value = if (user != null) {
                // Éxito: pasar nombre, email y rol
                LoginState.Success(
                    userName = user.nombre,
                    userEmail = user.email,
                    userRole = user.role
                )
            } else {
                LoginState.Error("Credenciales incorrectas o usuario no autorizado")
            }
        }
    }

    /**
     * Resetear estado de login (para cerrar sesión)
     */
    fun logout() {
        _loginState.value = LoginState.Idle
    }
}

/**
 * Representa los estados de la pantalla de login.
 */
sealed class LoginState {
    object Idle : LoginState()
    data class Success(
        val userName: String,
        val userEmail: String,
        val userRole: String
    ) : LoginState()
    data class Error(val message: String) : LoginState()
}
