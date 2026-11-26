# Diseño de la interfaz HTML

Archivo: `src/main/resources/web/index.html`. HTML define la estructura semántica de la página; no implementa el almacenamiento ni la lógica de negocio.

## 1. Cabecera del documento

```html
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="/style.css">
<script src="/app.js" defer></script>
```

UTF-8 permite representar tildes. viewport adapta el área visible a la pantalla. link carga CSS y script carga JavaScript. defer retrasa la ejecución del script hasta que se haya analizado el HTML. Las rutas comienzan por / porque los archivos los sirve Java desde el mismo origen.

## 2. Organización del cuerpo
`header` contiene la marca y descripción; `main` contiene el título, estadísticas y dos paneles. `.layout` agrupa el formulario y el listado. `section` organiza regiones temáticas; `h1` y `h2` definen jerarquía.

## 3. Formulario

```html
<label>Título<input name="title" required maxlength="100" placeholder="Ej. El equipo no arranca"></label>
```

`label` identifica el campo; `name` determina la clave enviada al backend; `required` y `maxlength` proporcionan validación en el navegador. La descripción usa textarea con máximo 1000 caracteres. El select `priority` permite Baja, Media y Alta (Media por defecto). El botón del formulario dispara submit. JavaScript intercepta ese evento. La validación HTML no sustituye la validación Java.

## 4. Conexión con CSS y JavaScript

| Identificador o clase | Uso |
|---|---|
| #ticket-form | JavaScript captura el envío |
| #stats | Contadores generados dinámicamente |
| #search | Búsqueda por texto |
| #filter | Filtro por estado |
| #tickets | Contenedor de tarjetas dinámicas |
| #message | Mensajes de operación |
| .panel, .layout, .stats | Estilos y distribución CSS |

Un id identifica un elemento; una clase puede reutilizarse. Los contenedores stats y tickets empiezan vacíos porque JavaScript los rellena. `aria-live="polite"`, `aria-label` y `role="status"` ayudan a comunicar información a tecnologías de asistencia.

## 5. Experimentos
- Cambia el h1 y recarga el navegador.
- Inspecciona #tickets antes y después de crear una incidencia con F12.
- Cambia temporalmente el id search y observa por qué JavaScript deja de encontrar el elemento. Restáuralo.
- Prueba enviar un título vacío y observa la validación del navegador.

No abras index.html directamente: las peticiones a la API necesitan el servidor Java.
