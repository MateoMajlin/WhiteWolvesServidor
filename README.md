# WhiteWolves

Desarrollador: Mateo Majlin

## Descripcion

El juego White Wolves, consiste de batallas 2v2 en las que 4 personajes se enfrentarán con el fin de
conseguir más bajas que el rival antes de que acabe el tiempo, los personajes evolucionan durante la partida, tanto por
conseguir equipamiento nuevo como por obtener nuevas habilidades a medida que pasa el tiempo.

## Tecnoligias Utilizadas

- LibGDX: Principal motor grafico.
- Intellij: Principal plataforma de desarrollo.

## Plataforma Objetivo
- Escritorio

## Como Compilar y Ejecutar el juego
- Prerrequisitos:

Tener un JDK (Java Development Kit) instalado (especificar versión si es crítica, ej. JDK 17+ porque Liftoff lo usa).
Tener Git instalado (para clonar).
Un IDE compatible con Gradle (IntelliJ IDEA o Eclipse).

- Pasos para Clonar:

git clone URL_DEL_REPOSITORIO

Pasos para Importar y Ejecutar en el IDE (el método más común para ellos y para ti):

- IntelliJ IDEA:
  "Abrir el proyecto (File > Open...) y seleccionar la carpeta raíz del proyecto clonado (o el archivo build.gradle)."
  "Esperar a que Gradle sincronice las dependencias."
  "Para ejecutar, buscar el módulo lwjgl3, y dentro de este, la clase [Lwjgl3Launcher.java]. Hacer clic derecho y seleccionar 'Run' o 'Debug'."
- Eclipse:
  "Importar el proyecto (File > Import... > Gradle > Existing Gradle Project) y seleccionar la carpeta raíz del proyecto clonado."
  "Esperar a que Gradle sincronice las dependencias."
  "Para ejecutar, buscar el módulo lwjgl3, y dentro de este, la clase [Lwjgl3Launcher.java]. Hacer clic derecho y seleccionar 'Run As > Java Application'."
  Pasos para Ejecutar mediante Gradle (alternativa, si aplica):

  "Desde la terminal, en la carpeta raíz del proyecto, ejecutar el comando: ./gradlew lwjgl3:run (o gradlew.bat lwjgl3:run en Windows)." (Esto asume que la tarea run está       configurada en su build.gradle para el módulo de escritorio, lo cual es común).

## Estado Actual del Proyecto
- Configuración inicial y estructura del proyecto
