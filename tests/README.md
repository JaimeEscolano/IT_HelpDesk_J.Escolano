# Pruebas manuales
Con el servidor iniciado:
1. Crear una incidencia válida: aparece con estado Abierta.
2. Intentar enviar campos vacíos: se rechaza.
3. Cambiar estado: se actualiza el contador.
4. Buscar un título y filtrar por estado: se muestran las coincidencias.
5. Reiniciar Java: se conservan los datos.
6. Eliminar una incidencia: desaparece y no vuelve tras reiniciar.
7. Abrir /api/tickets en el navegador: se obtiene JSON.
