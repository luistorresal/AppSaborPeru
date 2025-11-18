-- Script de datos iniciales (seed data)
-- Usuarios por defecto

INSERT IGNORE INTO users (id, nombre, apellido, dni, email, password, role) VALUES
(1, 'Luis', 'Torres', '12345678', 'luis@saborperu.cl', '123456', 'user'),
(2, 'Karla', 'Blanco', '87654321', 'karla@saborperu.cl', '123456', 'user'),
(3, 'Invitado', 'SaborPeru', '00000000', 'invitado@saborperu.cl', '123456', 'guest');

-- Productos por defecto
INSERT IGNORE INTO products (id, name, description, priceClp, isAvailable) VALUES
(1, 'Lomo Saltado', 'Lomo fino salteado con cebolla, tomate y papas.', 28000, TRUE),
(2, 'Ají de Gallina', 'Pollo deshilachado en crema de ají amarillo.', 24000, TRUE),
(3, 'Tallarín Saltado', 'Salteado al wok con verduras y salsa oriental.', 26000, TRUE),
(4, 'Causa Vegana Limeña', 'Capa de papa amarilla con relleno vegetal.', 22000, TRUE);

-- Resetear auto_increment después de insertar datos con IDs específicos
ALTER TABLE users AUTO_INCREMENT = 4;
ALTER TABLE products AUTO_INCREMENT = 5;

