# 🍽️ App Sabor Perú - CRUD con API REST

Aplicación Android con CRUD completo conectado a MySQL vía API REST.

## 📋 Requisitos previos

Antes de ejecutar el proyecto, necesitas instalar:

1. **Android Studio** (Arctic Fox o superior)
2. **Docker Desktop** - [Descargar aquí](https://www.docker.com/products/docker-desktop/)
3. **Node.js** (v18 o superior) - [Descargar aquí](https://nodejs.org/)
4. **MySQL Workbench** (opcional, para ver la BD) - [Descargar aquí](https://dev.mysql.com/downloads/workbench/)

## 🚀 Pasos para ejecutar

### Paso 1: Clonar el repositorio
```bash
git clone <URL_DEL_REPOSITORIO>
cd AppSaborPeru
```

### Paso 2: Iniciar MySQL con Docker
```bash
docker-compose up -d
```
Espera unos segundos a que MySQL inicie completamente.

### Paso 3: Iniciar la API REST
```bash
cd backend-api
npm install
node server.js
```
Deberías ver: `✅ Conectado a MySQL correctamente` y `🚀 API SaborPeru iniciada`

### Paso 4: Ejecutar la app en Android Studio
1. Abre Android Studio
2. Selecciona **Open** y elige la carpeta `AppSaborPeru`
3. Espera a que sincronice Gradle
4. Ejecuta la app en un **emulador** (importante: usar emulador, no dispositivo físico)

## 🔧 Configuración de conexión

### Para Emulador Android:
La app ya está configurada para conectarse a `10.0.2.2:8080` (localhost del host)

### Para Dispositivo Físico:
Edita `app/src/main/java/.../data/remote/api/RetrofitClient.kt`:
```kotlin
private const val BASE_URL = "http://TU_IP_LOCAL:8080/api/"
```

## 📊 Ver la Base de Datos (MySQL Workbench)

1. Abre MySQL Workbench
2. Crea nueva conexión:
   - **Host:** `127.0.0.1`
   - **Port:** `3306`
   - **Username:** `saborperu_user`
   - **Password:** `saborperu_pass`
3. Ejecuta: `SELECT * FROM saborperu_db.products;`

## ✅ Funcionalidades CRUD

| Operación | Descripción |
|-----------|-------------|
| **CREATE** | Botón + para agregar nuevos platos |
| **READ** | Lista de productos desde MySQL |
| **UPDATE** | Botón ✏️ para editar |
| **DELETE** | Botón 🗑️ para eliminar |

## 🧪 Ejecutar Pruebas

En Android Studio:
1. Navega a `app/src/test/java/.../ProductApiTest.kt`
2. Click derecho → **Run 'ProductApiTest'**

## 📁 Estructura del Proyecto

```
AppSaborPeru/
├── app/                    # Aplicación Android
│   └── src/main/java/.../
│       ├── data/remote/api/  # Retrofit + DTOs
│       ├── ui/products/      # Pantallas y ViewModel
│       └── ...
├── backend-api/            # API REST (Node.js + Express)
│   ├── server.js
│   └── package.json
├── docker/                 # Configuración MySQL
└── docker-compose.yml      # Para levantar MySQL
```

## 👥 Autores

- Proyecto de clase - Android Studio

## 📝 Notas

- La API debe estar corriendo antes de usar la app
- Usar emulador para que funcione la conexión `10.0.2.2`
- Docker Desktop debe estar iniciado
