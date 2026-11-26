# Diseño visual con CSS

Archivo: `src/main/resources/web/style.css`. CSS controla presentación, dimensiones, colores y distribución sin modificar los datos del servidor.

## 1. Sintaxis y selectores

```css
button{border:0;border-radius:6px;padding:11px 15px;background:#17639c;color:white;font:inherit;cursor:pointer}
```

`button` es el selector; cada declaración contiene propiedad y valor. El archivo está escrito de forma compacta, pero puede formatearse con saltos de línea sin alterar su comportamiento.

| Selector | Elementos |
|---|---|
| body | Fondo, tipografía y color general |
| .panel | Paneles de formulario y listado |
| .stat strong | Número dentro de cada contador |
| #message | Mensaje de estado |
| button:hover | Botón al pasar el puntero |
| button:disabled | Botón deshabilitado |
| .delete | Botón de eliminación |

La cascada resuelve reglas coincidentes mediante origen, importancia, especificidad y orden. Por ejemplo `.delete` tiene mayor especificidad que `button` y cambia su fondo.

## 2. Modelo de caja
`*{box-sizing:border-box}` hace que las dimensiones declaradas incluyan relleno y borde. `.panel` usa padding de 20px, borde de 1px y border-radius de 12px. `main` limita el ancho a 1200px y usa `margin:auto` para centrarse.

## 3. Distribución con Grid y Flexbox

```css
.layout{display:grid;grid-template-columns:1fr 2fr;gap:20px;align-items:start}
.stats{display:grid;grid-template-columns:repeat(3,1fr);gap:14px;margin:24px 0}
```

Grid distribuye los paneles en dos columnas de proporción 1:2 y las estadísticas en tres columnas iguales. Flexbox se utiliza en header, .filters y .actions para organizar elementos en una dimensión, con separación mediante gap.

## 4. Diseño adaptable

```css
@media(max-width:760px){.layout{grid-template-columns:1fr}.stats{gap:6px}.stat{padding:12px}.filters{flex-wrap:wrap}}
```

Cuando el viewport mide 760px o menos, los paneles pasan a una columna, se reducen los espacios de estadísticas y los filtros pueden saltar de línea.

## 5. Estilos dinámicos
JavaScript crea elementos con clases `stat`, `ticket`, `priority`, `actions` y `delete`. CSS se aplica también a esos elementos aunque no estén escritos inicialmente en index.html. `white-space:pre-wrap` conserva saltos de línea en descripciones y `overflow-wrap:anywhere` permite partir palabras largas.

## 6. Experimentos
- Cambia el color de header y button.
- Modifica 1fr 2fr por 1fr 1fr y compara los paneles.
- Usa F12 y el modo de dispositivo móvil para comprobar el punto de ruptura de 760px.
- Diseña una variante de alto contraste comprobando la legibilidad. No necesitas recompilar Java.
