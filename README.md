## Instalación y ejecución del proyecto

### 1. Requisitos previos

Para ejecutar IT-HelpDesk es necesario disponer de:

- Java JDK 17 o superior (recomendado JDK 21 LTS).
- Git.
- Un navegador web (Chrome, Edge, Firefox, etc.).


### 2. Instalación de Java

El JDK (Java Development Kit) incluye las herramientas necesarias
para compilar y ejecutar programas escritos en Java:

- `javac`: compilador de código Java.
- `java`: herramienta para ejecutar programas Java.

#### Opción A: Instalación desde PowerShell

Abrir una terminal de PowerShell y ejecutar:

```powershell
winget install --id EclipseAdoptium.Temurin.21.JDK -e
```

Esperar a que finalice la instalación.

#### Opción B: Instalación manual

1. Acceder a https://adoptium.net/temurin/releases/
2. Seleccionar las siguientes opciones:
   - Sistema operativo: Windows.
   - Arquitectura: x64.
   - Tipo de paquete: JDK.
   - Versión: 21 LTS.
3. Descargar el instalador en formato `.msi`.
4. Ejecutar el instalador.
5. Activar las opciones:
   - Add to PATH.
   - Set JAVA_HOME variable.
6. Finalizar la instalación.

### 3. Comprobar la instalación de Java

Cerrar completamente Visual Studio Code y volver a abrirlo.

Abrir una terminal y ejecutar:

```powershell
java -version
```

```powershell
javac -version
```

Ambos comandos deben mostrar la versión instalada.

Si alguno de los comandos no se reconoce, comprobar que Java
está correctamente instalado y añadido a la variable de entorno PATH.

### 4. Configuración de Visual Studio Code

Se recomienda instalar la extensión Extension Pack for Java.

1. Abrir Visual Studio Code.
2. Acceder al apartado de extensiones (Ctrl + Shift + X).
3. Buscar `Extension Pack for Java`.
4. Seleccionar la extensión publicada por Microsoft.
5. Pulsar Install.

Esta extensión facilita la escritura, ejecución y depuración
del código Java, pero no es obligatoria para ejecutar el proyecto.

### 5. Obtener el proyecto

Si el proyecto está publicado en GitHub, clonar el repositorio:

```bash
git clone URL_DEL_REPOSITORIO
```

Acceder a la carpeta descargada:

```bash
cd IT-HelpDesk
```

Abrir el proyecto en Visual Studio Code:

```bash
code .
```

Si se ha descargado el proyecto como archivo ZIP, descomprimirlo
y abrir directamente la carpeta IT-HelpDesk desde Visual Studio Code.

### 6. Compilar el código Java

Abrir una terminal en la raíz del proyecto y ejecutar:

```powershell
javac -encoding UTF-8 -d out src/main/java/HelpDesk.java
```

Este comando compila el código fuente Java y almacena los
archivos compilados `.class` en la carpeta `out/`.

Significado de los parámetros:

| Parámetro | Función |
|---|---|
| `javac` | Ejecuta el compilador de Java. |
| `-encoding UTF-8` | Establece la codificación de caracteres. |
| `-d out` | Indica dónde guardar los archivos compilados. |
| `src/main/java/HelpDesk.java` | Archivo de código fuente que se va a compilar. |

### 7. Ejecutar el servidor

Una vez compilado el código, ejecutar:

```powershell
java -cp out HelpDesk
```

Significado de los parámetros:

| Parámetro | Función |
|---|---|
| `java` | Ejecuta el programa Java. |
| `-cp out` | Indica dónde se encuentran las clases compiladas. |
| `HelpDesk` | Clase principal que se va a ejecutar. |

El servidor permanecerá en ejecución mientras la terminal
continúe abierta.

### 8. Acceder a la aplicación web

Abrir un navegador e introducir la siguiente dirección:

http://localhost:8080

Desde la aplicación podremos:

- Crear incidencias.
- Consultar incidencias.
- Asignar prioridades.
- Modificar estados.
- Buscar y filtrar incidencias.
- Eliminar incidencias.

No es necesario utilizar la extensión Live Server, ya que
el propio programa Java actúa como servidor web.

### 9. Detener el servidor

Para detener la aplicación, seleccionar la terminal en la que
se está ejecutando el servidor y pulsar:

Ctrl + C

### 10. Ejecutar el proyecto después de realizar cambios

Dependiendo del archivo modificado, será necesario realizar
diferentes acciones:

| Archivo modificado | Acción necesaria |
|---|---|
| HTML (`.html`) | Actualizar el navegador. |
| CSS (`.css`) | Actualizar el navegador. |
| JavaScript (`.js`) | Actualizar el navegador. |
| Java (`.java`) | Detener el servidor, recompilar y volver a ejecutarlo. |
| Markdown (`.md`) | No requiere reiniciar el servidor. |

Para recompilar y ejecutar Java:

```powershell
javac -encoding UTF-8 -d out src/main/java/HelpDesk.java
java -cp out HelpDesk
```

### 11. Solución de problemas frecuentes

| Problema | Posible causa | Solución |
|---|---|---|
| `java` no se reconoce | Java no está instalado o no está en PATH. | Instalar el JDK y reiniciar VS Code. |
| `javac` no se reconoce | No está instalado el JDK o no está en PATH. | Instalar y configurar el JDK. |
| `File not found` | La terminal está en un directorio incorrecto. | Situarse en la raíz del proyecto. |
| `Address already in use` | El puerto 8080 está ocupado. | Detener el proceso que utiliza ese puerto. |
| No se puede acceder a localhost | El servidor no está funcionando. | Ejecutar el servidor Java. |
| Los cambios en Java no aparecen | El código no se ha recompilado. | Detener, compilar y ejecutar nuevamente. |
| Los cambios en CSS o JavaScript no aparecen | El navegador utiliza archivos en caché. | Pulsar Ctrl + F5. |

### 12. Archivos generados automáticamente

Durante la compilación y ejecución del proyecto se generan
los siguientes directorios:

- `out/`: contiene los archivos compilados de Java.
- `data/`: contiene los datos locales de las incidencias.

Estos directorios no deben subirse al repositorio de GitHub.
Para ello, se deben excluir mediante el archivo `.gitignore`.

El código fuente sí debe estar versionado, ya que permite
a cualquier desarrollador compilar y ejecutar el proyecto
en su propio ordenador.