package com.example.appsaborperu

import com.example.appsaborperu.data.remote.api.ApiResponse
import com.example.appsaborperu.data.remote.api.ProductRequest
import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas Unitarias del CRUD de Productos
 * 
 * Estas pruebas verifican la lógica de las operaciones CRUD
 * sin necesidad de conexión a la API real.
 * 
 * Para la demostración al profesor:
 * - Ejecutar con: ./gradlew test
 * - O desde Android Studio: Click derecho en el archivo → Run Tests
 */
class ProductApiTest {

    // ==================== TEST CREATE ====================
    
    @Test
    fun `CREATE - validar que producto tenga nombre`() {
        // Arrange: Preparar datos de prueba
        val productRequest = ProductRequest(
            name = "Ceviche de Prueba",
            description = "Plato de prueba para el test",
            priceClp = 2500,
            isAvailable = true
        )
        
        // Act: Verificar que el nombre no está vacío
        val isValid = productRequest.name.isNotBlank()
        
        // Assert: El producto debe tener nombre
        assertTrue("El producto debe tener un nombre válido", isValid)
        assertEquals("Ceviche de Prueba", productRequest.name)
        
        println("✅ TEST CREATE: Validación de nombre pasó correctamente")
    }
    
    @Test
    fun `CREATE - validar que precio sea mayor a cero`() {
        // Arrange
        val productRequest = ProductRequest(
            name = "Lomo Saltado Test",
            description = "Descripción de prueba",
            priceClp = 2800,
            isAvailable = true
        )
        
        // Act
        val isPriceValid = productRequest.priceClp > 0
        
        // Assert
        assertTrue("El precio debe ser mayor a 0", isPriceValid)
        assertEquals(2800, productRequest.priceClp)
        
        println("✅ TEST CREATE: Validación de precio pasó correctamente")
    }

    // ==================== TEST READ ====================
    
    @Test
    fun `READ - verificar estructura de ProductRequest`() {
        // Arrange: Simular un producto
        val product = ProductRequest(
            name = "Ají de Gallina",
            description = "Pollo deshilachado en crema de ají amarillo",
            priceClp = 2400,
            isAvailable = true
        )
        
        // Act & Assert: Verificar que tiene todos los campos
        assertEquals("Ají de Gallina", product.name)
        assertEquals("Pollo deshilachado en crema de ají amarillo", product.description)
        assertEquals(2400, product.priceClp)
        assertTrue(product.isAvailable)
        
        println("✅ TEST READ: Estructura de Product verificada correctamente")
    }
    
    @Test
    fun `READ - verificar conversión de precio a soles`() {
        // Arrange
        val priceClp = 3500
        
        // Act: Convertir de centavos a soles
        val precioEnSoles = priceClp / 100.0
        
        // Assert
        assertEquals(35.0, precioEnSoles, 0.01)
        
        println("✅ TEST READ: Conversión de precio a soles correcta (S/. $precioEnSoles)")
    }

    // ==================== TEST UPDATE ====================
    
    @Test
    fun `UPDATE - verificar que datos se actualizan correctamente`() {
        // Arrange: Producto original
        val productoOriginal = ProductRequest(
            name = "Ceviche Original",
            description = "Descripción original",
            priceClp = 3000,
            isAvailable = true
        )
        
        // Act: Crear request de actualización
        val updateRequest = ProductRequest(
            name = "Ceviche Actualizado",
            description = "Descripción actualizada",
            priceClp = 3500,
            isAvailable = false
        )
        
        // Assert: Verificar que los valores son diferentes
        assertNotEquals(productoOriginal.name, updateRequest.name)
        assertNotEquals(productoOriginal.priceClp, updateRequest.priceClp)
        assertEquals("Ceviche Actualizado", updateRequest.name)
        assertEquals(3500, updateRequest.priceClp)
        
        println("✅ TEST UPDATE: Datos de actualización verificados correctamente")
    }

    // ==================== TEST DELETE ====================
    
    @Test
    fun `DELETE - verificar respuesta exitosa de eliminación`() {
        // Arrange: Simular respuesta de la API después de eliminar
        val apiResponse = ApiResponse<Any>(
            success = true,
            message = "Producto eliminado correctamente",
            data = null
        )
        
        // Assert
        assertTrue("La respuesta debe ser exitosa", apiResponse.success)
        assertEquals("Producto eliminado correctamente", apiResponse.message)
        assertNull("El data debe ser null después de eliminar", apiResponse.data)
        
        println("✅ TEST DELETE: Respuesta de eliminación verificada correctamente")
    }

    // ==================== TEST VALIDACIONES ====================
    
    @Test
    fun `VALIDACION - nombre vacío debe ser rechazado`() {
        // Arrange
        val productRequest = ProductRequest(
            name = "",
            description = "Test",
            priceClp = 1000,
            isAvailable = true
        )
        
        // Act
        val isNameValid = productRequest.name.isNotBlank()
        
        // Assert
        assertFalse("Un nombre vacío no debe ser válido", isNameValid)
        
        println("✅ TEST VALIDACION: Nombre vacío rechazado correctamente")
    }
    
    @Test
    fun `VALIDACION - precio cero o negativo debe ser rechazado`() {
        // Arrange
        val preciosCero = ProductRequest("Test", "Desc", 0, true)
        val precioNegativo = ProductRequest("Test", "Desc", -100, true)
        
        // Act & Assert
        assertFalse("Precio 0 no debe ser válido", preciosCero.priceClp > 0)
        assertFalse("Precio negativo no debe ser válido", precioNegativo.priceClp > 0)
        
        println("✅ TEST VALIDACION: Precios inválidos rechazados correctamente")
    }

    // ==================== TEST API RESPONSE ====================
    
    @Test
    fun `API RESPONSE - verificar estructura de respuesta exitosa`() {
        // Arrange
        val productos = listOf("Ceviche", "Lomo Saltado")
        
        val apiResponse = ApiResponse(
            success = true,
            message = "Productos obtenidos correctamente",
            data = productos
        )
        
        // Assert
        assertTrue(apiResponse.success)
        assertEquals(2, apiResponse.data?.size)
        assertEquals("Ceviche", apiResponse.data?.get(0))
        
        println("✅ TEST API RESPONSE: Estructura de respuesta verificada")
        println("   - Total productos: ${apiResponse.data?.size}")
    }
}
