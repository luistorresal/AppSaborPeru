# 📱 Instrucciones para Probar la Aplicación en Android Studio

## ✅ Compilación Exitosa

El proyecto se ha compilado correctamente. El APK de debug está disponible en:
```
app/build/outputs/apk/debug/app-debug.apk
```

## 🚀 Pasos para Probar en Android Studio

### 1. Abrir el Proyecto en Android Studio

1. Abre **Android Studio**
2. Selecciona **File → Open**
3. Navega a la carpeta del proyecto: `C:\Users\CETECOM\Downloads\App_Peru`
4. Haz clic en **OK** y espera a que Android Studio sincronice el proyecto

### 2. Verificar la Configuración

1. Verifica que el proyecto se haya sincronizado correctamente (barra de estado en la parte inferior)
2. Si hay errores de sincronización, haz clic en **Sync Now** o **File → Sync Project with Gradle Files**

### 3. Configurar un Emulador o Dispositivo

#### Opción A: Usar Emulador Android

1. Ve a **Tools → Device Manager** (o haz clic en el ícono del dispositivo en la barra superior)
2. Si no tienes un emulador:
   - Haz clic en **Create Device**
   - Selecciona un dispositivo (ej: Pixel 5)
   - Selecciona una imagen del sistema (API 24 o superior)
   - Completa la configuración
3. Inicia el emulador haciendo clic en el botón ▶️

#### Opción B: Usar Dispositivo Físico

1. Habilita **Opciones de desarrollador** en tu dispositivo Android:
   - Ve a **Configuración → Acerca del teléfono**
   - Toca **Número de compilación** 7 veces
2. Habilita **Depuración USB**:
   - Ve a **Configuración → Opciones de desarrollador**
   - Activa **Depuración USB**
3. Conecta tu dispositivo por USB
4. Acepta el diálogo de depuración USB en el dispositivo

### 4. Ejecutar la Aplicación

1. En la barra superior de Android Studio, selecciona tu dispositivo/emulador del dropdown
2. Haz clic en el botón **Run** (▶️) o presiona **Shift + F10**
3. Espera a que la aplicación se compile e instale

## 🧪 Pruebas del Carrito (Funcionalidad Corregida)

### Prueba 1: Agregar Productos al Carrito

1. En la pantalla de **Catálogo de productos**
2. Haz clic en **Agregar** en cualquier producto
3. Verifica que el producto aparezca en la sección **🛒 Carrito de compras**

### Prueba 2: Incrementar Cantidad (Botón +)

1. Con al menos un producto en el carrito
2. Haz clic en el botón **+** junto al producto
3. **VERIFICA**: La cantidad debe aumentar inmediatamente (ej: de 1 a 2, de 2 a 3, etc.)
4. El precio total también debe actualizarse automáticamente

### Prueba 3: Decrementar Cantidad (Botón -)

1. Con un producto que tenga cantidad > 1
2. Haz clic en el botón **-** junto al producto
3. **VERIFICA**: La cantidad debe disminuir (ej: de 2 a 1)
4. Si la cantidad es 1 y presionas **-**, el producto debe eliminarse del carrito

### Prueba 4: Agregar Múltiples Productos

1. Agrega varios productos diferentes al carrito
2. Incrementa la cantidad de cada uno usando el botón **+**
3. **VERIFICA**: Cada producto mantiene su cantidad independiente
4. El total debe reflejar la suma correcta de todos los productos

### Prueba 5: Eliminar Producto

1. Agrega un producto al carrito
2. Haz clic en el botón **🗑️** (papelera)
3. **VERIFICA**: El producto debe desaparecer del carrito inmediatamente

### Prueba 6: Vaciar Carrito

1. Agrega varios productos al carrito
2. Haz clic en el botón **Vaciar carrito**
3. **VERIFICA**: Todos los productos deben desaparecer y el total debe ser 0

## 🔍 Verificar que Funciona Correctamente

### ✅ Indicadores de Éxito:

- ✅ Al presionar **+**, la cantidad aumenta **inmediatamente** sin necesidad de recargar
- ✅ El número de cantidad se actualiza en tiempo real
- ✅ El precio total se recalcula automáticamente
- ✅ No hay retrasos o "lag" en la actualización de la UI
- ✅ La aplicación no se congela o crashea

### ❌ Si No Funciona:

1. **Verifica los logs**:
   - Abre **Logcat** en Android Studio (pestaña inferior)
   - Filtra por "ProductViewModel" o busca errores en rojo
   
2. **Limpia y reconstruye**:
   - **Build → Clean Project**
   - **Build → Rebuild Project**
   - Vuelve a ejecutar la aplicación

3. **Verifica que el código esté actualizado**:
   - Asegúrate de que el archivo `ProductViewModel.kt` tenga los cambios recientes
   - Verifica que `CartItem.quantity` sea `val` (no `var`)

## 📝 Notas Adicionales

- **MySQL**: Si planeas probar la conexión a MySQL, asegúrate de que el contenedor Docker esté corriendo (ver `MYSQL_SETUP.md`)
- **Emulador vs Dispositivo**: Para probar MySQL, usa el emulador con host `10.0.2.2` o un dispositivo físico con la IP de tu máquina
- **Logs**: Si encuentras problemas, revisa Logcat para ver mensajes de error o advertencias

## 🐛 Solución de Problemas Comunes

### Error: "Gradle sync failed"
- **Solución**: File → Invalidate Caches / Restart → Invalidate and Restart

### Error: "Device not found"
- **Solución**: Verifica que el dispositivo esté conectado y con depuración USB activada

### La app no se actualiza después de cambios
- **Solución**: Build → Clean Project, luego Build → Rebuild Project

### El botón + aún no funciona
- **Solución**: Verifica que hayas aceptado los cambios en `ProductViewModel.kt` y que el proyecto se haya recompilado

---

## ✨ Cambios Realizados en Esta Versión

- ✅ Corregido el problema del botón **+** que no incrementaba la cantidad
- ✅ Cambiado `CartItem.quantity` de `var` a `val` (inmutable)
- ✅ Implementado reemplazo de elementos en la lista para notificar cambios a Compose
- ✅ Mejorada la reactividad de la UI del carrito

¡Disfruta probando la aplicación! 🎉

