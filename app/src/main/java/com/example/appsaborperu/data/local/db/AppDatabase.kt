package com.example.appsaborperu.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appsaborperu.data.local.dao.*
import com.example.appsaborperu.data.local.entity.*

/**
 * Base de datos Room principal del proyecto SaborPeru.
 * - Define las entidades (tablas)
 * - Expone los DAOs (acceso a datos)
 */
@Database(
    entities = [
        UserEntity::class,
        ProductEntity::class,
        CartItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs disponibles en la BD
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
}
