const express = require('express');
const mysql = require('mysql2/promise');
const cors = require('cors');

const app = express();
const PORT = 8080;

// Middleware
app.use(cors());
app.use(express.json());

// Configuración de MySQL
const dbConfig = {
    host: 'localhost',
    port: 3306,
    user: 'saborperu_user',
    password: 'saborperu_pass',
    database: 'saborperu_db'
};

// Pool de conexiones
let pool;

async function initDatabase() {
    try {
        pool = mysql.createPool(dbConfig);
        console.log('✅ Conectado a MySQL correctamente');
        
        // Insertar datos semilla si la tabla está vacía
        const [rows] = await pool.query('SELECT COUNT(*) as count FROM products');
        if (rows[0].count === 0) {
            console.log('📦 Insertando datos semilla...');
            await pool.query(`
                INSERT INTO products (name, description, priceClp, isAvailable) VALUES
                ('Lomo Saltado', 'Lomo fino salteado con cebolla, tomate y papas fritas.', 2800, true),
                ('Ají de Gallina', 'Pollo deshilachado en crema de ají amarillo con arroz.', 2400, true),
                ('Ceviche Clásico', 'Pescado fresco marinado en limón con cebolla y camote.', 3200, true),
                ('Arroz con Mariscos', 'Arroz con variedad de mariscos al estilo norteño.', 3500, true)
            `);
            console.log('✅ Datos semilla insertados');
        }
    } catch (error) {
        console.error('❌ Error al conectar a MySQL:', error.message);
        process.exit(1);
    }
}

// ==================== ENDPOINTS CRUD ====================

// GET /api/products - Obtener todos los productos (READ ALL)
app.get('/api/products', async (req, res) => {
    try {
        const [products] = await pool.query('SELECT * FROM products ORDER BY name ASC');
        res.json({
            success: true,
            message: 'Productos obtenidos correctamente',
            data: products
        });
        console.log(`📖 READ: ${products.length} productos obtenidos`);
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Error al obtener productos: ' + error.message
        });
    }
});

// GET /api/products/:id - Obtener un producto por ID (READ ONE)
app.get('/api/products/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const [products] = await pool.query('SELECT * FROM products WHERE id = ?', [id]);
        
        if (products.length === 0) {
            return res.status(404).json({
                success: false,
                message: 'Producto no encontrado'
            });
        }
        
        res.json({
            success: true,
            message: 'Producto encontrado',
            data: products[0]
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Error al obtener producto: ' + error.message
        });
    }
});

// POST /api/products - Crear un producto (CREATE)
app.post('/api/products', async (req, res) => {
    try {
        const { name, description, priceClp, isAvailable } = req.body;
        
        // Validaciones
        if (!name || name.trim() === '') {
            return res.status(400).json({
                success: false,
                message: 'El nombre es requerido'
            });
        }
        
        if (!priceClp || priceClp <= 0) {
            return res.status(400).json({
                success: false,
                message: 'El precio debe ser mayor a 0'
            });
        }
        
        const [result] = await pool.query(
            'INSERT INTO products (name, description, priceClp, isAvailable) VALUES (?, ?, ?, ?)',
            [name, description || '', priceClp, isAvailable !== false]
        );
        
        const newProduct = {
            id: result.insertId,
            name,
            description: description || '',
            priceClp,
            isAvailable: isAvailable !== false
        };
        
        res.status(201).json({
            success: true,
            message: 'Producto creado correctamente',
            data: newProduct
        });
        
        console.log(`✅ CREATE: Producto '${name}' creado con ID ${result.insertId}`);
        
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Error al crear producto: ' + error.message
        });
    }
});

// PUT /api/products/:id - Actualizar un producto (UPDATE)
app.put('/api/products/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const { name, description, priceClp, isAvailable } = req.body;
        
        // Validaciones
        if (!name || name.trim() === '') {
            return res.status(400).json({
                success: false,
                message: 'El nombre es requerido'
            });
        }
        
        if (!priceClp || priceClp <= 0) {
            return res.status(400).json({
                success: false,
                message: 'El precio debe ser mayor a 0'
            });
        }
        
        const [result] = await pool.query(
            'UPDATE products SET name = ?, description = ?, priceClp = ?, isAvailable = ? WHERE id = ?',
            [name, description || '', priceClp, isAvailable !== false, id]
        );
        
        if (result.affectedRows === 0) {
            return res.status(404).json({
                success: false,
                message: 'Producto no encontrado'
            });
        }
        
        const updatedProduct = {
            id: parseInt(id),
            name,
            description: description || '',
            priceClp,
            isAvailable: isAvailable !== false
        };
        
        res.json({
            success: true,
            message: 'Producto actualizado correctamente',
            data: updatedProduct
        });
        
        console.log(`✅ UPDATE: Producto ID ${id} actualizado`);
        
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Error al actualizar producto: ' + error.message
        });
    }
});

// DELETE /api/products/:id - Eliminar un producto (DELETE)
app.delete('/api/products/:id', async (req, res) => {
    try {
        const { id } = req.params;
        
        const [result] = await pool.query('DELETE FROM products WHERE id = ?', [id]);
        
        if (result.affectedRows === 0) {
            return res.status(404).json({
                success: false,
                message: 'Producto no encontrado'
            });
        }
        
        res.json({
            success: true,
            message: 'Producto eliminado correctamente'
        });
        
        console.log(`✅ DELETE: Producto ID ${id} eliminado`);
        
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Error al eliminar producto: ' + error.message
        });
    }
});

// Health check
app.get('/health', (req, res) => {
    res.json({ status: 'OK', message: 'API SaborPeru funcionando correctamente' });
});

// Iniciar servidor
initDatabase().then(() => {
    app.listen(PORT, () => {
        console.log(`
🚀 ========================================
   API SaborPeru iniciada correctamente
   Puerto: ${PORT}
   
   Endpoints disponibles:
   - GET    /api/products      (Listar todos)
   - GET    /api/products/:id  (Obtener uno)
   - POST   /api/products      (Crear)
   - PUT    /api/products/:id  (Actualizar)
   - DELETE /api/products/:id  (Eliminar)
   - GET    /health            (Estado)
========================================
        `);
    });
});

