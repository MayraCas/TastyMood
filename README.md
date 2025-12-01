# TastyMood 🍽️

TastyMood es una aplicación Android que recomienda recetas basadas en tu estado de ánimo. La app te ayuda a encontrar la comida perfecta según cómo te sientes, permitiéndote personalizar las recomendaciones con preferencias dietéticas e ingredientes.

## 📱 Características

- **Recomendaciones basadas en el ánimo**: Selecciona cómo te sientes (Feliz, Triste, Enojado) y obtén recetas personalizadas
- **Filtros de dieta**: Soporte para diferentes estilos de alimentación (vegetariano, basado en animales, sin azúcares)
- **Preferencias de ingredientes**: Incluye o excluye ingredientes específicos en las recomendaciones
- **Recetas favoritas**: Guarda tus recetas favoritas para acceso rápido
- **Detalles de recetas**: Visualiza ingredientes y pasos de preparación

## 🛠️ Tecnologías

| Tecnología | Uso |
|------------|-----|
| **Kotlin** | Lenguaje de programación principal |
| **Jetpack Compose** | Framework moderno para UI declarativa |
| **Room Database** | Persistencia local de recetas y favoritos |
| **DataStore** | Almacenamiento de preferencias de usuario |
| **Navigation Compose** | Navegación entre pantallas |
| **Coil** | Carga asíncrona de imágenes |
| **Coroutines** | Programación asíncrona |
| **Material 3** | Componentes de diseño moderno |

## 📋 Requisitos

- Android Studio Hedgehog o superior
- JDK 11 o superior
- Android SDK 24+ (Android 7.0 Nougat) como mínimo
- Android SDK 35 como target

## 🚀 Cómo ejecutar el proyecto

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/MayraCas/TastyMood.git
   cd TastyMood
   ```

2. **Abrir en Android Studio**
   - Abre Android Studio
   - Selecciona `File > Open`
   - Navega hasta la carpeta del proyecto y selecciónala

3. **Sincronizar dependencias**
   - Android Studio sincronizará automáticamente las dependencias de Gradle
   - Si no lo hace automáticamente, haz clic en `Sync Project with Gradle Files`

4. **Ejecutar la aplicación**
   - Conecta un dispositivo Android o inicia un emulador
   - Haz clic en el botón `Run` (▶️) o presiona `Shift + F10`

## 📁 Estructura del proyecto

```
app/src/main/
├── java/com/example/tastymood/
│   ├── MainActivity.kt          # Pantalla de splash
│   ├── PantallaRegistro.kt      # Registro de usuario
│   ├── PantallaHome.kt          # Pantalla principal con selección de ánimo
│   ├── Navegación.kt            # Configuración de navegación
│   ├── Recetas.kt               # Lista de recetas
│   ├── RecetasDetalle.kt        # Detalle de una receta
│   ├── RecetasFavoritas.kt      # Recetas guardadas como favoritas
│   ├── Configuracion.kt         # Configuración de la app
│   ├── RecetaViewModel.kt       # ViewModel para manejo de datos
│   ├── database/                # Entidades y DAOs de Room
│   ├── model/                   # Modelos de datos
│   ├── ui/                      # Componentes de UI y temas
│   └── utils/                   # Utilidades (DataStoreManager)
└── res/                         # Recursos (layouts, drawables, strings)
```

## 🔧 Compilar el proyecto

Para compilar el proyecto desde la línea de comandos:

```bash
# En Linux/macOS
./gradlew assembleDebug

# En Windows
gradlew.bat assembleDebug
```

El APK generado estará en `app/build/outputs/apk/debug/`

## 📄 Licencia

Este proyecto es de código abierto. Siéntete libre de usarlo y modificarlo según tus necesidades.
