package com.example.routes

import com.example.models.*
import com.example.plugins.Products
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.productRoutes() {
    routing {
        route("/api/products") {
            
            // ==================== READ ALL ====================
            get {
                val products = transaction {
                    Products.selectAll()
                        .orderBy(Products.name)
                        .map { row ->
                            Product(
                                id = row[Products.id],
                                name = row[Products.name],
                                description = row[Products.description] ?: "",
                                priceClp = row[Products.priceClp],
                                isAvailable = row[Products.isAvailable]
                            )
                        }
                }
                call.respond(ApiResponse(
                    success = true,
                    message = "Productos obtenidos correctamente",
                    data = products
                ))
            }
            
            // ==================== READ BY ID ====================
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ApiResponse<Product>(
                        success = false,
                        message = "ID inválido"
                    ))
                    return@get
                }
                
                val product = transaction {
                    Products.select { Products.id eq id }
                        .map { row ->
                            Product(
                                id = row[Products.id],
                                name = row[Products.name],
                                description = row[Products.description] ?: "",
                                priceClp = row[Products.priceClp],
                                isAvailable = row[Products.isAvailable]
                            )
                        }
                        .firstOrNull()
                }
                
                if (product == null) {
                    call.respond(HttpStatusCode.NotFound, ApiResponse<Product>(
                        success = false,
                        message = "Producto no encontrado"
                    ))
                } else {
                    call.respond(ApiResponse(
                        success = true,
                        message = "Producto encontrado",
                        data = product
                    ))
                }
            }
            
            // ==================== CREATE ====================
            post {
                try {
                    val request = call.receive<ProductRequest>()
                    
                    // Validaciones
                    if (request.name.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, ApiResponse<Product>(
                            success = false,
                            message = "El nombre es requerido"
                        ))
                        return@post
                    }
                    
                    if (request.priceClp <= 0) {
                        call.respond(HttpStatusCode.BadRequest, ApiResponse<Product>(
                            success = false,
                            message = "El precio debe ser mayor a 0"
                        ))
                        return@post
                    }
                    
                    val newId = transaction {
                        Products.insert {
                            it[name] = request.name
                            it[description] = request.description
                            it[priceClp] = request.priceClp
                            it[isAvailable] = request.isAvailable
                        } get Products.id
                    }
                    
                    val newProduct = Product(
                        id = newId,
                        name = request.name,
                        description = request.description,
                        priceClp = request.priceClp,
                        isAvailable = request.isAvailable
                    )
                    
                    call.respond(HttpStatusCode.Created, ApiResponse(
                        success = true,
                        message = "Producto creado correctamente",
                        data = newProduct
                    ))
                    
                    println("✅ CREATE: Producto '${request.name}' creado con ID $newId")
                    
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, ApiResponse<Product>(
                        success = false,
                        message = "Error al crear producto: ${e.message}"
                    ))
                }
            }
            
            // ==================== UPDATE ====================
            put("/{id}") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, ApiResponse<Product>(
                            success = false,
                            message = "ID inválido"
                        ))
                        return@put
                    }
                    
                    val request = call.receive<ProductRequest>()
                    
                    // Validaciones
                    if (request.name.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, ApiResponse<Product>(
                            success = false,
                            message = "El nombre es requerido"
                        ))
                        return@put
                    }
                    
                    if (request.priceClp <= 0) {
                        call.respond(HttpStatusCode.BadRequest, ApiResponse<Product>(
                            success = false,
                            message = "El precio debe ser mayor a 0"
                        ))
                        return@put
                    }
                    
                    val updated = transaction {
                        Products.update({ Products.id eq id }) {
                            it[name] = request.name
                            it[description] = request.description
                            it[priceClp] = request.priceClp
                            it[isAvailable] = request.isAvailable
                        }
                    }
                    
                    if (updated == 0) {
                        call.respond(HttpStatusCode.NotFound, ApiResponse<Product>(
                            success = false,
                            message = "Producto no encontrado"
                        ))
                    } else {
                        val updatedProduct = Product(
                            id = id,
                            name = request.name,
                            description = request.description,
                            priceClp = request.priceClp,
                            isAvailable = request.isAvailable
                        )
                        call.respond(ApiResponse(
                            success = true,
                            message = "Producto actualizado correctamente",
                            data = updatedProduct
                        ))
                        
                        println("✅ UPDATE: Producto ID $id actualizado")
                    }
                    
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, ApiResponse<Product>(
                        success = false,
                        message = "Error al actualizar producto: ${e.message}"
                    ))
                }
            }
            
            // ==================== DELETE ====================
            delete("/{id}") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, ApiResponse<Product>(
                            success = false,
                            message = "ID inválido"
                        ))
                        return@delete
                    }
                    
                    val deleted = transaction {
                        Products.deleteWhere { Products.id eq id }
                    }
                    
                    if (deleted == 0) {
                        call.respond(HttpStatusCode.NotFound, ApiResponse<Product>(
                            success = false,
                            message = "Producto no encontrado"
                        ))
                    } else {
                        call.respond(ApiResponse<Product>(
                            success = true,
                            message = "Producto eliminado correctamente"
                        ))
                        
                        println("✅ DELETE: Producto ID $id eliminado")
                    }
                    
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, ApiResponse<Product>(
                        success = false,
                        message = "Error al eliminar producto: ${e.message}"
                    ))
                }
            }
        }
        
        // Endpoint de salud
        get("/health") {
            call.respond(mapOf("status" to "OK", "message" to "API SaborPeru funcionando"))
        }
    }
}

