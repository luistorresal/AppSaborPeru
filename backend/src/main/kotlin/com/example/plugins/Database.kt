package com.example.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

// Tabla de productos
object Products : Table("products") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val priceClp = integer("priceClp")
    val isAvailable = bool("isAvailable").default(true)
    
    override val primaryKey = PrimaryKey(id)
}

fun Application.configureDatabase() {
    Database.connect(
        url = "jdbc:mysql://localhost:3306/saborperu_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "saborperu_user",
        password = "saborperu_pass"
    )
    
    // Crear tabla si no existe
    transaction {
        SchemaUtils.create(Products)
        
        // Insertar datos semilla si la tabla está vacía
        if (Products.selectAll().count() == 0L) {
            Products.insert {
                it[name] = "Lomo Saltado"
                it[description] = "Lomo fino salteado con cebolla, tomate y papas fritas."
                it[priceClp] = 2800
                it[isAvailable] = true
            }
            Products.insert {
                it[name] = "Ají de Gallina"
                it[description] = "Pollo deshilachado en crema de ají amarillo con arroz."
                it[priceClp] = 2400
                it[isAvailable] = true
            }
            Products.insert {
                it[name] = "Ceviche Clásico"
                it[description] = "Pescado fresco marinado en limón con cebolla y camote."
                it[priceClp] = 3200
                it[isAvailable] = true
            }
            Products.insert {
                it[name] = "Arroz con Mariscos"
                it[description] = "Arroz con variedad de mariscos al estilo norteño."
                it[priceClp] = 3500
                it[isAvailable] = true
            }
        }
    }
    
    println("✅ Base de datos MySQL conectada correctamente")
}

