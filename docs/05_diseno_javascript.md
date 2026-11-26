# Diseño del frontend JavaScript

Archivo: `src/main/resources/web/app.js`. JavaScript conecta la interfaz HTML con la API Java, gestiona eventos y actualiza el DOM (representación de la página en memoria).

## 1. Referencias y estado

```javascript
const form = document.querySelector('#ticket-form');
const list = document.querySelector('#tickets');
let tickets = [];
```

querySelector busca elementos por selector CSS. const impide reasignar la referencia; let permite sustituir el array tickets cuando llegan datos nuevos. Este array es una copia en el navegador; el almacenamiento persistente pertenece al backend.

## 2. Comunicación HTTP

```javascript
async function request(path = '', method = 'GET', data) {
    const options = {method};
    if (data) options.body = new URLSearchParams(data);
    const response = await fetch('/api/tickets' + path, options);
    const result = await response.json();
    if (!response.ok) throw new Error(result.error || 'Error del servidor');
    return result;
}
```

fetch realiza una petición HTTP asíncrona. await espera su resultado dentro de la función async sin bloquear toda la interfaz. URLSearchParams codifica los datos del formulario como application/x-www-form-urlencoded; el servidor devuelve JSON, que response.json() convierte en valores JavaScript. response.ok comprueba si el estado HTTP es de éxito.

## 3. Renderizado
`element(tag, text, className)` crea un nodo con createElement, asigna textContent y opcionalmente una clase CSS. textContent trata el texto de usuarios como texto, no como HTML ejecutable. `render()` limpia los contenedores con replaceChildren(), calcula los tres contadores y crea tarjetas para las incidencias visibles. Cada tarjeta contiene título, prioridad, descripción, selector de estado y botón de eliminación.

## 4. Búsqueda y filtros

```javascript
const query = search.value.toLowerCase();
const visible = tickets.filter(t => (!filter.value || t.status === filter.value) && (t.title + ' ' + t.description).toLowerCase().includes(query));
```

filter devuelve un nuevo array con elementos que cumplen una condición. Se exige que el estado coincida (o no haya filtro) y que título o descripción concatenados contengan la búsqueda sin distinguir mayúsculas. Los contadores se calculan sobre todas las incidencias, no solo las visibles. Buscar no envía peticiones HTTP adicionales.

## 5. Eventos y operaciones

| Evento | Acción |
|---|---|
| submit del formulario | preventDefault evita la navegación; FormData recoge campos; POST crea la incidencia |
| input de search | render() actualiza la búsqueda |
| change de filter | render() actualiza el filtro |
| change del selector de tarjeta | PATCH envía el nuevo estado |
| click en Eliminar | confirm solicita confirmación y DELETE elimina |

Durante el envío del formulario se deshabilita su botón. `perform(action)` ejecuta la operación, llama a refresh() y muestra un mensaje; si hay error lo comunica y vuelve a renderizar. `refresh()` solicita GET, actualiza tickets y llama a render(). La última línea del archivo inicia la primera carga y captura errores.

## 6. Ejemplo de flujo completo
Usuario pulsa Registrar → submit → request('', 'POST', new FormData(form)) → Java valida y guarda → respuesta JSON → form.reset() → refresh() → GET → render() → nueva tarjeta y contadores.

## 7. Experimentos
- Abre F12 > Network, crea una incidencia y observa POST seguido de GET. Inspecciona Payload y Response.
- Busca por texto y verifica que no se genera una petición nueva.
- Cambia el mensaje de confirmación de eliminación y recarga.
- Añade un filtro por prioridad: crea un select en HTML, referencia su id en JS, incorpora su condición a filter y registra el evento change. No es necesario modificar Java porque priority ya llega en el JSON.
