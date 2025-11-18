package com.example.appsaborperu.data.remote.example

import com.example.appsaborperu.data.remote.MySQLConnection
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Ejemplo de uso de la conexión MySQL.
 * 
 * NOTA: Este es solo un ejemplo. Para producción, se recomienda usar
 * un backend API REST con Retrofit en lugar de conexión directa.
 */
object MySQLExample {
    
    private const val TAG = "MySQLExample"
    
    /**
     * Ejemplo: Obtener todos los usuarios de la base de datos.
     */
    suspend fun getAllUsers(): List<Map<String, Any>> = withContext(Dispatchers.IO) {
        MySQLConnection.withConnection { connection ->
            val users = mutableListOf<Map<String, Any>>()
            val query = "SELECT * FROM users"
            
            connection.prepareStatement(query).use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        users.add(mapOf(
                            "id" to rs.getInt("id"),
                            "nombre" to rs.getString("nombre"),
                            "apellido" to rs.getString("apellido"),
                            "email" to rs.getString("email"),
                            "role" to rs.getString("role")
                        ))
                    }
                }
            }
            users
        } ?: emptyList()
    }
    
    /**
     * Ejemplo: Obtener todos los productos.
     */
    suspend fun getAllProducts(): List<Map<String, Any>> = withContext(Dispatchers.IO) {
        MySQLConnection.withConnection { connection ->
            val products = mutableListOf<Map<String, Any>>()
            val query = "SELECT * FROM products WHERE isAvailable = 1"
            
            connection.prepareStatement(query).use { stmt ->
                stmt.executeQuery().use { rs ->
                    while (rs.next()) {
                        products.add(mapOf(
                            "id" to rs.getInt("id"),
                            "name" to rs.getString("name"),
                            "description" to rs.getString("description"),
                            "priceClp" to rs.getInt("priceClp"),
                            "isAvailable" to rs.getBoolean("isAvailable")
                        ))
                    }
                }
            }
            products
        } ?: emptyList()
    }
    
    /**
     * Ejemplo: Verificar conexión.
     */
    suspend fun testConnection(): Boolean = withContext(Dispatchers.IO) {
        val isConnected = MySQLConnection.testConnection()
        Log.d(TAG, "Conexión MySQL: ${if (isConnected) "OK" else "FALLIDA"}")
        isConnected
    }
}

