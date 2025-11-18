# Configuración de MySQL con Docker

## Requisitos
- Docker y Docker Compose instalados

## Inicio rápido

1. **Copiar el archivo de configuración** (opcional, si quieres cambiar valores por defecto):
   ```bash
   cp .env.example .env
   ```

2. **Levantar MySQL en Docker**:
   ```bash
   docker-compose up -d
   ```

3. **Verificar que el contenedor está corriendo**:
   ```bash
   docker-compose ps
   ```

4. **Ver los logs**:
   ```bash
   docker-compose logs -f mysql
   ```

5. **Conectarse a MySQL** (opcional):
   ```bash
   docker exec -it saborperu_mysql mysql -u root -p
   # Password: rootpassword (o el que hayas configurado)
   ```

## Detener MySQL

```bash
docker-compose down
```

## Detener y eliminar volúmenes (elimina todos los datos)

```bash
docker-compose down -v
```

## Configuración de conexión desde Android

### Para Emulador Android:
- Host: `10.0.2.2`
- Puerto: `3306`
- Base de datos: `saborperu_db`
- Usuario: `saborperu_user`
- Password: `saborperu_pass`

### Para Dispositivo Físico:
- Host: IP de tu máquina (ej: `192.168.1.100`)
- Puerto: `3306`
- Base de datos: `saborperu_db`
- Usuario: `saborperu_user`
- Password: `saborperu_pass`

## Estructura de la base de datos

- **users**: Usuarios del sistema
- **products**: Catálogo de productos
- **cart_items**: Items del carrito de compras

Los scripts de inicialización se ejecutan automáticamente al crear el contenedor por primera vez.

