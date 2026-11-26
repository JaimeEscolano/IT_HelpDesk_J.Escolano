# Diseño del backend Java

Archivo: `src/main/java/HelpDesk.java`. Es un servidor HTTP local sin frameworks externos. Una sola clase agrupa el modelo, las rutas, la validación, la persistencia y el servicio de archivos. Es una decisión de simplicidad educativa, no una arquitectura de producción.

## 1. Modelo de datos

```java
record Ticket(String id, String title, String description, String priority, String status) {}
```

Un record representa datos mediante componentes y accesores como `t.title()`. Sus componentes no se reasignan. Cada incidencia tiene identificador UUID, título, descripción, prioridad y estado. Para cambiar el estado se crea otro Ticket con el mismo identificador.

`Map<String, Ticket> tickets = new LinkedHashMap<>()` relaciona identificadores con incidencias y conserva el orden de inserción. Los conjuntos `priorities` y `statuses` definen valores admitidos.

## 2. Inicio del servidor

```java
load();
HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
server.createContext("/api/tickets", HelpDesk::api);
server.createContext("/", HelpDesk::web);
server.start();
```

`load()` recupera los datos. `HttpServer` pertenece al JDK. `127.0.0.1` limita el acceso a la máquina local. Las referencias a métodos `HelpDesk::api` y `HelpDesk::web` indican qué código atiende las peticiones. `web()` solo permite GET y sirve index.html, style.css y app.js con sus tipos MIME correspondientes.

## 3. API y lógica de negocio
`api(HttpExchange x)` obtiene la ruta y el método HTTP. Es `synchronized` para evitar que dos operaciones sobre el mapa se ejecuten simultáneamente.

| Método | Ruta | Acción | Éxito |
|---|---|---|---|
| GET | /api/tickets | Lista todas las incidencias | 200 |
| POST | /api/tickets | Crea una incidencia | 201 |
| PATCH | /api/tickets/{id} | Cambia el estado | 200 |
| DELETE | /api/tickets/{id} | Elimina | 200 |

POST usa `form(x)` para leer campos codificados como formulario URL. Comprueba título no vacío (máximo 100 caracteres), descripción no vacía (máximo 1000) y prioridad válida. Genera un UUID y establece el estado Abierta. PATCH comprueba el estado permitido; DELETE retira la entrada del mapa. No existe una operación para editar título o descripción.

## 4. Respuestas y errores
`q()` escapa caracteres para producir cadenas JSON válidas; `json()` serializa un Ticket. `send()` establece cabeceras y escribe bytes; `reply()` envía JSON y `error()` construye un objeto con el campo error. Los códigos 400, 404, 405 y 500 indican datos inválidos, recurso inexistente, método no permitido y error de almacenamiento respectivamente. `form()` limita el cuerpo a 16384 bytes.

## 5. Persistencia
`DATA` apunta a `data/tickets.tsv`. `load()` lee líneas separadas por tabuladores. `save(next)` crea el directorio, escribe el nuevo conjunto en `tickets.tmp`, sustituye el fichero y actualiza el mapa en memoria. Título y descripción se codifican en Base64 mediante `encode()` y `decode()` para conservar caracteres especiales sin romper el formato TSV. Base64 no cifra ni protege secretos. El archivo temporal reduce riesgos de escritura incompleta, pero no se garantiza una sustitución atómica.

## 6. Recorrido de una incidencia
Formulario → POST → form() → validación → Ticket → save() → JSON 201 → JavaScript actualiza la lista.

## 7. Experimentos
- Con el servidor iniciado, abre http://localhost:8080/api/tickets y observa el JSON.
- Crea una incidencia, detén el servidor y examina data/tickets.tsv. Reinicia y verifica que se conserva.
- Cambia el límite del título en Java y en HTML de forma coherente. Recompila y prueba.

## 8. Limitaciones
No hay autenticación, autorización, base de datos ni edición completa. El diseño está pensado para uso local con datos ficticios. Una futura refactorización podría separar Ticket, almacenamiento y controlador HTTP en clases distintas.
