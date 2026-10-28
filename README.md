# MPAZ PRO 6.º

## Descripción del proyecto
Aplicación móvil Android desarrollada en Kotlin que apoya el trabajo pedagógico de **Sexto Básico** en la Escuela Escritora Marcela Paz (San Bernardo). Organiza contenidos explicativos, una prueba final y un informe por **Objetivo de Aprendizaje (OA)** para las asignaturas de Lenguaje y Comunicación, Matemática, Ciencias Naturales e Historia.

Reemplaza las guías impresas por un recorrido digital: el docente asigna objetivos y define cuántos intentos tiene la prueba, el estudiante estudia y rinde, y la aplicación genera un informe por objetivo que el docente debe **validar**. La jefatura de UTP revisa y autoriza el contenido antes de que pueda usarse.

> **Alcance:** es un Producto Mínimo Viable. Usa únicamente datos ficticios y no corresponde a una plataforma productiva.

## Estudiantes | GRUPO 15
- Pier Villacorta Aguilar
- Elizabeth Troncoso Volpi

## Perfiles de usuario
| Perfil | Qué puede hacer |
|---|---|
| **UTP** | Revisar contenidos y preguntas de cada objetivo, y autorizarlos o retirar la autorización |
| **Docente** | Asignar objetivos autorizados a su curso, configurar el límite de intentos de la prueba final, consultar los informes y validarlos |
| **Estudiante** | Ver los contenidos asignados, rendir la prueba final dentro del límite de intentos y revisar sus respuestas correctas e incorrectas. No tiene acceso al informe pedagógico |

## Funcionalidades implementadas
- [x] Inicio de sesión con perfiles (UTP, Docente, Estudiante) y navegación según el rol
- [x] Registro de sesión para perfiles (Docente y Estudiante)
- [x] Persistencia local de usuarios, objetivos, contenidos, preguntas, respuestas e informes
- [x] **Estudiante:** lista de asignaturas asignadas, contenidos explicativos por objetivo y prueba final con una pregunta por pantalla
- [x] Límite de intentos de la prueba configurable por el docente, con mensaje cuando se agotan
- [x] Resultado de la prueba con respuestas correctas e incorrectas
- [x] **Docente:** asignación de objetivos autorizados a su curso, configuración de intentos y consulta de informes por estudiante
- [x] Informe pedagógico y **validación manual obligatoria** del docente
- [x] **UTP:** revisión del detalle de cada objetivo y autorización de contenidos
- [x] Interfaz con tema azul, `Scaffold`, barra de navegación inferior para el docente y animaciones de transición
- [x] Descarga del informe 
- [x] Actividades y desafíos previos a la prueba final
- [x] Contenido de las cuatro asignaturas 

## Tecnologías utilizadas
- Android Studio / Kotlin
- Jetpack Compose con Material 3
- Navigation Compose (`NavController`, `NavHost`, navegación anidada por pestañas)
- ViewModel y corrutinas de Kotlin
- Base de datos: **Room sobre SQLite** (local, con datos ficticios)
- KSP (procesador de anotaciones para Room)

## Requisitos previos
- Android Studio: Android Studio Rabbit 1 | 2026.2.1 (o superior)
- JDK: Java Development Kit (JDK) 64-bit 
- SDK de Android : 24
- Un emulador o un teléfono Android con depuración USB activada

## Pasos para ejecutar
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/PierVillacorta/proyectoMpazpro.git
   ```
2. Abrir la carpeta del proyecto con Android Studio (**File → Open**).
3. Esperar a que termine la sincronización de Gradle (**Sync Now** si aparece el aviso).
4. Elegir un dispositivo (emulador o teléfono) y pulsar **Run**.
5. En la primera ejecución la aplicación crea la base de datos y carga los datos de prueba automáticamente.

## Usuarios de prueba
Todos los datos son **ficticios**. La contraseña de todos es `1234`.

| Usuario | Perfil | Curso |
|---|---|---|
| `utp.demo` | UTP | – |
| `docente.demo` | Docente | 6.º A |
| `alumno1` | Estudiante | 6.º A |
| `alumno2` | Estudiante | 6.º A |

## Recorrido sugerido para probar
1. Ingresar como `utp.demo` y autorizar el objetivo de Ciencias Naturales (viene *en revisión*).
2. Ingresar como `docente.demo`, activar los objetivos que se quieren asignar y definir los intentos de la prueba.
3. Ingresar como `alumno1`, revisar el contenido y rendir la prueba. Repetir con `alumno2`.
4. Volver como `docente.demo`, abrir la pestaña **Informes** y validar los informes pendientes.

## Estructura del proyecto
```
app/src/main/java/cl/duoc/mpazpro/
├── MainActivity.kt
├── data/local/
│   ├── AppDatabase.kt          # Base de datos Room
│   ├── DatosIniciales.kt       # Datos ficticios de arranque
│   ├── entity/                 # Tablas
│   ├── dao/                    # Consultas
│   ├── dto/                    # Resultados de consultas con JOIN
│   └── enums/                  # Roles, estados, asignaturas, etc.
├── ui/
│   ├── components/             # Scaffold reutilizable
│   ├── navigation/             # Rutas y NavHost
│   ├── login/
│   ├── estudiante/
│   ├── docente/
│   ├── utp/
│   └── theme/                  # Colores, formas y tipografía
└── util/                       # Generador de informes
```

## Reglas de negocio destacadas
- Solo el contenido **autorizado** por UTP puede ser asignado por el docente y visto por el estudiante.
- El número de intentos se cuenta por estudiante y por unidad, y lo define el docente.
- El informe clasifica cada objetivo según el porcentaje de respuestas correctas: **fortaleza** (70 % o más), **en desarrollo** (entre 50 % y 69 %) y **necesita refuerzo** (menos de 50 %). Estos umbrales son una decisión del equipo.
- El informe nace como *pendiente de validación* y requiere la validación del docente. La aplicación **no emite diagnósticos** ni toma decisiones académicas automáticas.
- El estudiante nunca ve el informe pedagógico.

## Restricciones del proyecto
- Datos 100 % ficticios: sin nombres, RUT, correos ni notas reales.
- No se solicitan permisos de cámara, ubicación, micrófono ni datos biométricos.
- No se usa inteligencia artificial generativa ni servicios externos.
- Los contenidos son propios, públicos o autorizados; no se copian guías ni libros protegidos.
- No hay conexión con sistemas ni bases de datos reales de la escuela.

## Notas para desarrollo
- Cada vez que se modifica una entidad de Room hay que **subir el número de `version`** en `AppDatabase`. La base usa migración destructiva: al subir la versión se recrea y se vuelven a cargar los datos de prueba.

