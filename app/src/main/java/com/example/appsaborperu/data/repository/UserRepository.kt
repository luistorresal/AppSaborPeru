package com.example.appsaborperu.data.repository

import com.example.appsaborperu.data.local.dao.UserDao
import com.example.appsaborperu.data.local.entity.UserEntity

/**
 * Repositorio de Usuarios.
 * - Expone login(email, password) para la pantalla de Login.
 * - Carga usuarios semilla (3 usuarios específicos).
 */
class UserRepository(
    private val userDao: UserDao
) {
    // Lista de usuarios autorizados
    private val authorizedUsers = listOf(
        UserEntity(
            id = 0,
            nombre = "Marines",
            apellido = "Admin",
            dni = "11111111",
            email = "marines@saborperu.cl",
            password = "123456",
            role = "admin"
        ),
        UserEntity(
            id = 0,
            nombre = "Claudio",
            apellido = "Admin",
            dni = "22222222",
            email = "claudio@saborperu.cl",
            password = "123456",
            role = "admin"
        ),
        UserEntity(
            id = 0,
            nombre = "Luis",
            apellido = "Cliente",
            dni = "33333333",
            email = "luis@saborperu.cl",
            password = "123456",
            role = "cliente"
        )
    )

    /**
     * Inserta los 3 usuarios autorizados.
     * Limpia usuarios existentes y los vuelve a crear para asegurar datos correctos.
     */
    suspend fun seedIfEmpty() {
        // Siempre asegurar que los 3 usuarios existan
        val count = userDao.count()
        if (count < 3) {
            // Si hay menos de 3, limpiar y recrear
            userDao.deleteAll()
            userDao.insertAll(authorizedUsers)
        }
    }

    /**
     * Valida credenciales contra los usuarios autorizados.
     * Primero intenta en BD local, si no encuentra, valida contra lista hardcodeada.
     */
    suspend fun login(email: String, password: String): UserEntity? {
        // Intentar buscar en BD
        val userFromDb = userDao.getByEmail(email)
        if (userFromDb != null && userFromDb.password == password) {
            return userFromDb
        }
        
        // Si no está en BD, validar contra lista autorizada y agregarlo
        val authorizedUser = authorizedUsers.find { 
            it.email.equals(email, ignoreCase = true) && it.password == password 
        }
        
        if (authorizedUser != null) {
            // Insertar el usuario si no existía
            userDao.insert(authorizedUser)
            return authorizedUser
        }
        
        return null
    }
}
