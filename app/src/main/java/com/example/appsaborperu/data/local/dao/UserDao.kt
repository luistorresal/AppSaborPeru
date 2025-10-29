package com.example.appsaborperu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsaborperu.data.local.entity.UserEntity

/**
 * DAO de usuarios.
 * - Permite obtener un usuario por email (para el login).
 * - Insertar usuarios de ejemplo (semilla inicial).
 * - Contar usuarios (para saber si ya se inicializó la BD).
 */
@Dao
interface UserDao {

    /** Busca un usuario por su correo electrónico. */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    /** Inserta una lista de usuarios; ignora duplicados por email. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<UserEntity>)

    /** Cuenta cuántos usuarios existen en la tabla. */
    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int
}
