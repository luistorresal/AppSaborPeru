package com.example.appsaborperu.data.remote

import com.example.appsaborperu.data.remote.config.DatabaseConfig
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Clase para manejar conexiones directas a MySQL.
 * 
 * NOTA: La conexión directa a MySQL desde Android no es recomendada para producción.
 * Se recomienda usar un backend API REST con Retrofit.
 * 
 * Esta clase es útil para desarrollo y testing.
 */
object MySQLConnection {
    
    /**
     * Obtiene una conexión a la base de datos MySQL.
     * @return Connection o null si hay error
     */
    fun getConnection(): Connection? {
        return try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver")
            
            // Establecer conexión
            DriverManager.getConnection(
                DatabaseConfig.JDBC_URL,
                DatabaseConfig.MYSQL_USER,
                DatabaseConfig.MYSQL_PASSWORD
            )
        } catch (e: ClassNotFoundException) {
            android.util.Log.e("MySQLConnection", "Driver MySQL no encontrado", e)
            null
        } catch (e: SQLException) {
            android.util.Log.e("MySQLConnection", "Error al conectar a MySQL", e)
            null
        } catch (e: Exception) {
            android.util.Log.e("MySQLConnection", "Error inesperado", e)
            null
        }
    }
    
    /**
     * Ejecuta una operación con conexión automática.
     * La conexión se cierra automáticamente después de la operación.
     */
    inline fun <T> withConnection(block: (Connection) -> T): T? {
        val connection = getConnection() ?: return null
        return try {
            block(connection)
        } catch (e: Exception) {
            android.util.Log.e("MySQLConnection", "Error en operación", e)
            null
        } finally {
            try {
                connection.close()
            } catch (e: SQLException) {
                android.util.Log.e("MySQLConnection", "Error al cerrar conexión", e)
            }
        }
    }
    
    /**
     * Verifica si la conexión a MySQL está disponible.
     */
    fun testConnection(): Boolean {
        return withConnection { connection ->
            !connection.isClosed
        } ?: false
    }
}

