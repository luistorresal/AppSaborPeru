package com.example.appsaborperu.data.remote.config

/**
 * Configuración de conexión a MySQL.
 * 
 * IMPORTANTE:
 * - Para emulador Android: usar host = "10.0.2.2"
 * - Para dispositivo físico: usar la IP de tu máquina (ej: "192.168.1.100")
 * - Para producción: usar un backend API REST con Retrofit (recomendado)
 */
object DatabaseConfig {
    // Configuración para emulador Android
    const val MYSQL_HOST_EMULATOR = "10.0.2.2"
    
    // Configuración para dispositivo físico (cambiar por tu IP)
    const val MYSQL_HOST_DEVICE = "192.168.1.100" // Cambiar por tu IP local
    
    // Configuración por defecto
    const val MYSQL_HOST = MYSQL_HOST_EMULATOR
    const val MYSQL_PORT = 3306
    const val MYSQL_DATABASE = "saborperu_db"
    const val MYSQL_USER = "saborperu_user"
    const val MYSQL_PASSWORD = "saborperu_pass"
    
    // URL de conexión JDBC
    val JDBC_URL = "jdbc:mysql://$MYSQL_HOST:$MYSQL_PORT/$MYSQL_DATABASE?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
    
    // URL base para API REST (si usas un backend)
    const val API_BASE_URL = "http://$MYSQL_HOST:8080/api/" // Ejemplo con backend en puerto 8080
}

