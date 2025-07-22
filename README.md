# WhiteWolves

**Desarrollador:** Mateo Majlin  
**[🌐 Wiki del Proyecto](https://github.com/MateoMajlin/WhiteWolves/wiki)**

## Descripción

El juego *White Wolves* consiste en batallas 2v2 en las que 4 personajes se enfrentarán con el fin de conseguir más bajas que el rival antes de que acabe el tiempo.  
Los personajes evolucionan durante la partida, tanto por conseguir equipamiento nuevo como por obtener nuevas habilidades a medida que pasa el tiempo.

## Estado del Proyecto
- Primer prototipo inicial jugable disponible.

### Caracterisiticas Implementadas

- Pantalla de inicio donde seleccionar iniciar partida, opciones o creditos.
- Entorno jugable con colisiones con el mapa.
- Personaje jugable con animacion y sistema para atacar.
- Cajas que el personaje puede destruir golpeandolas.
- Uso de Box2D para gestion de colisiones.
- Uso de mapa Tiled para cargar graficos de fondo, paredes y props estaticos.

## Tecnologías Utilizadas y Plataforma Objetivo

- **LibGDX:** Principal motor gráfico.
- **IntelliJ:** Plataforma principal de desarrollo.
- **Java:** Lenguaje de Programacion
- **Box 2D:** Biblioteca de Fisica para gestion de colisiones.
- Escritorio (PC/Laptop)

## Cómo Compilar y Ejecutar el Juego

### Prerrequisitos

- Tener un **JDK** (Java Development Kit) instalado. *(Ej.: JDK 17+, requerido por Liftoff)*
- Tener **Git** instalado.
- Usar un **IDE compatible con Gradle** (como IntelliJ IDEA o Eclipse).

### Pasos para Clonar

git clone https://github.com/MateoMajlin/WhiteWolves.git

### Pasos para Importar y Ejecutar en el IDE:

**IntelliJ IDEA:**

"Abrir el proyecto (File > Open...) y seleccionar la carpeta raíz del proyecto clonado (o el archivo build.gradle)."
"Esperar a que Gradle sincronice las dependencias."
"Para ejecutar, buscar el módulo lwjgl3, y dentro de este, la clase [Lwjgl3Launcher.java]. Hacer clic derecho y seleccionar 'Run' o 'Debug'."

**Eclipse:**

"Importar el proyecto (File > Import... > Gradle > Existing Gradle Project) y seleccionar la carpeta raíz del proyecto clonado."
"Esperar a que Gradle sincronice las dependencias."
"Para ejecutar, buscar el módulo lwjgl3 (o el nombre que corresponda), y dentro de este, la clase [Lwjgl3Launcher.java]. Hacer clic derecho y seleccionar 'Run As > Java Application'."
Pasos para Ejecutar mediante Gradle (alternativa, si aplica):

"Desde la terminal, en la carpeta raíz del proyecto, ejecutar el comando: ./gradlew lwjgl3:run (o gradlew.bat lwjgl3:run en Windows)." (Esto asume que la tarea run está configurada en su build.gradle para el módulo de escritorio, lo cual es común).

