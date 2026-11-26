# Compilación y ejecución de Java

## 1. Del código fuente al programa
Java es un lenguaje cuyo código fuente (`.java`) se compila normalmente a bytecode (`.class`). El bytecode no es directamente código máquina de Windows: la JVM lo carga, verifica y ejecuta, pudiendo compilar partes a código máquina mediante JIT.

```text
HelpDesk.java → javac → archivos .class → JVM → servidor HTTP
```

El JDK incluye el compilador `javac`, el lanzador `java` y las herramientas y bibliotecas necesarias. La JVM es el entorno de ejecución del bytecode. Un entorno que solo permite ejecutar Java no necesariamente incluye el compilador.

## 2. Compilar el proyecto
Desde la raíz del repositorio, con JDK 17 o superior:

```powershell
javac -encoding UTF-8 -d out src/main/java/HelpDesk.java
```

| Elemento | Función |
|---|---|
| javac | Analiza sintaxis, tipos y referencias y genera bytecode |
| -encoding UTF-8 | Interpreta correctamente caracteres del código fuente |
| -d out | Directorio de salida |
| src/main/java/HelpDesk.java | Código fuente de entrada |

Se generan `out/HelpDesk.class` y `out/HelpDesk$Ticket.class`: el segundo corresponde al record declarado dentro de HelpDesk. El compilador no ejecuta el servidor. Si hay errores de sintaxis o tipos, deben corregirse antes de ejecutar la nueva versión.

## 3. Ejecutar

```powershell
java -cp out HelpDesk
```

`-cp out` establece el classpath, es decir, dónde buscar las clases. `HelpDesk` es el nombre de la clase, sin extensión. Java invoca su método `public static void main(String[] args)`. El servidor escucha en 127.0.0.1:8080. Abre http://localhost:8080 y detén el proceso con Ctrl+C.

## 4. Qué ocurre con los otros lenguajes
HTML y CSS se interpretan en el navegador; JavaScript se ejecuta en su motor. No se incluyen en la compilación con javac. El servidor lee estos archivos desde `src/main/resources/web` cuando recibe peticiones. Por eso se ejecuta desde la raíz del proyecto.

## 5. Cambios y Git
Tras modificar Java, detén el servidor, recompila y vuelve a ejecutar. Tras modificar HTML, CSS o JS, recarga el navegador (Ctrl+F5 si hay caché). Git debe versionar el código fuente, no `out/` ni los `.class`, que se pueden regenerar. El `.gitignore` ya los excluye.

## 6. Experimentos
1. Compila y ejecuta `Get-ChildItem out` en PowerShell. Identifica las dos clases.
2. Ejecuta `javap -c -classpath out HelpDesk` para observar instrucciones de bytecode.
3. Cambia el texto de `System.out.println` en main. Comprueba que guardar el fuente no actualiza el bytecode; recompila y reinicia.
4. Introduce temporalmente un error de sintaxis y observa el diagnóstico de javac. Deshaz el cambio.
5. Ejecuta `git status --short --ignored` y comprueba que out/ se ignora.
