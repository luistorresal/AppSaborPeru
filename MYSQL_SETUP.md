# Configuración de MySQL con Docker

## 📋 Requisitos Previos

- Docker Desktop instalado y corriendo
- Docker Compose instalado (incluido en Docker Desktop)

## 🚀 Inicio Rápido

### 1. Levantar MySQL en Docker

**Windows:**
```bash
start-mysql.bat
```

**Linux/Mac:**
```bash
./start-mysql.sh
```

**O manualmente:**
```bash
docker-compose up -d
```

### 2. Verificar que MySQL está corriendo

```bash
docker-compose ps
```

Deberías ver el contenedor `saborperu_mysql` con estado `Up`.

### 3. Ver los logs (opcional)

```bash
docker-compose logs -f mysql
```

### 4. Conectarse a MySQL (opcional)

```bash
docker exec -it saborperu_mysql mysql -u root -p
# Password: rootpassword
```

## 🔧 Configuración

### Variables de Entorno

Puedes crear un archivo `.env` en la raíz del proyecto para personalizar la configuración:

```env
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=saborperu_db
MYSQL_USER=saborperu_user
MYSQL_PASSWORD=saborperu_pass
MYSQL_PORT=3306
```

Si no creas el archivo `.env`, se usarán los valores por defecto.

### Configuración desde Android

#### Para Emulador Android:
- **Host:** `10.0.2.2`
- **Puerto:** `3306`
- **Base de datos:** `saborperu_db`
- **Usuario:** `saborperu_user`
- **Password:** `saborperu_pass`

#### Para Dispositivo Físico:
1. Encuentra la IP de tu máquina:
   - **Windows:** `ipconfig` (busca IPv4)
   - **Linux/Mac:** `ifconfig` o `ip addr`
2. Actualiza `DatabaseConfig.kt` con tu IP:
   ```kotlin
   const val MYSQL_HOST_DEVICE = "192.168.1.XXX" // Tu IP
   ```
3. Asegúrate de que tu dispositivo Android esté en la misma red WiFi.

## 📊 Estructura de la Base de Datos

La base de datos se inicializa automáticamente con:

### Tablas:
- **users**: Usuarios del sistema
- **products**: Catálogo de productos
- **cart_items**: Items del carrito de compras

### Datos Iniciales:
- 3 usuarios de ejemplo
- 4 productos de ejemplo

## 🔌 Conexión desde la Aplicación Android

### Opción 1: Conexión Directa (Desarrollo)

La aplicación incluye `MySQLConnection.kt` para conexión directa:

```kotlin
import com.example.appsaborperu.data.remote.MySQLConnection

// Ejemplo de uso
MySQLConnection.withConnection { connection ->
    // Tu código SQL aquí
    val stmt = connection.prepareStatement("SELECT * FROM users")
    val rs = stmt.executeQuery()
    // Procesar resultados...
}
```

**⚠️ Nota:** La conexión directa no es recomendada para producción.

### Opción 2: API REST con Retrofit (Recomendado)

Para producción, se recomienda crear un backend API REST. Las dependencias de Retrofit ya están incluidas en el proyecto.

## 🛠️ Comandos Útiles

### Detener MySQL
```bash
docker-compose down
```

### Detener y eliminar todos los datos
```bash
docker-compose down -v
```

### Reiniciar MySQL
```bash
docker-compose restart
```

### Ver estado
```bash
docker-compose ps
```

### Acceder a la consola MySQL
```bash
docker exec -it saborperu_mysql mysql -u saborperu_user -p saborperu_db
# Password: saborperu_pass
```

## 🐛 Solución de Problemas

### El contenedor no inicia
- Verifica que Docker Desktop esté corriendo
- Verifica que el puerto 3306 no esté en uso: `netstat -an | findstr 3306` (Windows) o `lsof -i :3306` (Linux/Mac)

### No puedo conectar desde Android
- **Emulador:** Asegúrate de usar `10.0.2.2` como host
- **Dispositivo físico:** Verifica que esté en la misma red WiFi y que el firewall permita conexiones en el puerto 3306

### Error de permisos
- Verifica las credenciales en `DatabaseConfig.kt`
- Verifica que el usuario tenga permisos en la base de datos

## 📝 Archivos Importantes

- `docker-compose.yml`: Configuración de Docker
- `docker/mysql/init/01-init-schema.sql`: Script de creación de tablas
- `docker/mysql/init/02-seed-data.sql`: Datos iniciales
- `app/src/main/java/.../data/remote/config/DatabaseConfig.kt`: Configuración de conexión
- `app/src/main/java/.../data/remote/MySQLConnection.kt`: Clase de conexión

## 🔐 Seguridad

⚠️ **IMPORTANTE:** Esta configuración es para desarrollo. Para producción:

1. Cambia todas las contraseñas por defecto
2. Usa un backend API REST en lugar de conexión directa
3. Implementa autenticación y autorización
4. Usa SSL/TLS para las conexiones
5. No expongas MySQL directamente a internet

