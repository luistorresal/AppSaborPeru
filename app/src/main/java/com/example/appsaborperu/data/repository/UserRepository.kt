package com.example.appsaborperu.data.repository

import com.example.appsaborperu.data.local.dao.UserDao
import com.example.appsaborperu.data.local.entity.UserEntity

/**
 * Repositorio de Usuarios.
 * - Expone login(email, password) para la pantalla de Login.
 * - Carga usuarios semilla si la tabla está vacía (3 usuarios).
 *
 * Nota: password en texto para demo. Puedes cambiar a hash más adelante.
 */
class UserRepository(
    private val userDao: UserDao
) {

    /**
     * Inserta los 3 usuarios por defecto SOLO si la tabla está vacía.
     * Llamar una vez al iniciar la app (por ejemplo, en el AuthViewModel).
     */
    suspend fun seedIfEmpty() {
        val count = userDao.count()
        if (count == 0) {
            val defaults = listOf(
                UserEntity(
                    id = 0,
                    nombre = "Luis",
                    apellido = "Torres",
                    dni = "12345678",
                    email = "luis@saborperu.cl",
                    password = "123456",
                    role = "user"
                ),
                UserEntity(
                    id = 0,
                    nombre = "Karla",
                    apellido = "Blanco",
                    dni = "87654321",
                    email = "karla@saborperu.cl",
                    password = "123456",
                    role = "user"
                ),
                UserEntity(
                    id = 0,
                    nombre = "Invitado",
                    apellido = "SaborPeru",
                    dni = "00000000",
                    email = "invitado@saborperu.cl",
                    password = "123456",
                    role = "guest"
                )
            )
            userDao.insertAll(defaults)
        }
    }

    /**
     * Valida credenciales básicas. Devuelve el usuario si son correctas, o null si no.
     * Para "Entrar como invitado" no necesitas llamar a login; puedes crear un UserEntity
     * temporal en el ViewModel con role = "guest".
     */
    suspend fun login(email: String, password: String): UserEntity? {
        val user = userDao.getByEmail(email) ?: return null
        return if (user.password == password) user else null
    }
}
