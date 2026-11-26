const form = document.querySelector('#ticket-form');
const list = document.querySelector('#tickets');
const search = document.querySelector('#search');
const filter = document.querySelector('#filter');
const message = document.querySelector('#message');
let tickets = [];
async function request(path = '', method = 'GET', data) {
    const options = {method};
    if (data) options.body = new URLSearchParams(data);
    const response = await fetch('/api/tickets' + path, options);
    const result = await response.json();
    if (!response.ok) throw new Error(result.error || 'Error del servidor');
    return result;
}
function element(tag, text, className) {
    const node = document.createElement(tag);
    node.textContent = text;
    if (className) node.className = className;
    return node;
}
function render() {
    const stats = document.querySelector('#stats');
    stats.replaceChildren();
    for (const status of ['Abierta', 'En curso', 'Resuelta']) {
        const card = element('div', '', 'stat');
        card.append(element('strong', tickets.filter(t => t.status === status).length), element('span', status));
        stats.append(card);
    }
    list.replaceChildren();
    const query = search.value.toLowerCase();
    const visible = tickets.filter(t => (!filter.value || t.status === filter.value) && (t.title + ' ' + t.description).toLowerCase().includes(query));
    if (!visible.length) list.append(element('p', 'No hay incidencias que mostrar.'));
    for (const t of visible) {
        const card = element('article', '', 'ticket');
        card.append(element('h3', t.title), element('span', 'Prioridad: ' + t.priority, 'priority'), element('p', t.description));
        const actions = element('div', '', 'actions');
        const select = document.createElement('select');
        select.setAttribute('aria-label', 'Estado de ' + t.title);
        for (const status of ['Abierta', 'En curso', 'Resuelta']) select.add(new Option(status, status));
        select.value = t.status;
        select.onchange = () => perform(async () => { await request('/' + t.id, 'PATCH', {status: select.value}); });
        const del = element('button', 'Eliminar', 'delete');
        del.onclick = () => { if (confirm('¿Eliminar esta incidencia?')) perform(() => request('/' + t.id, 'DELETE')); };
        actions.append(select, del); card.append(actions); list.append(card);
    }
}
async function refresh() { tickets = await request(); render(); }
async function perform(action) {
    try { await action(); await refresh(); message.textContent = 'Cambios guardados.'; }
    catch (error) { message.textContent = error.message; render(); }
}
form.addEventListener('submit', async event => {
    event.preventDefault();
    const button = form.querySelector('button'); button.disabled = true;
    await perform(async () => { await request('', 'POST', new FormData(form)); form.reset(); });
    button.disabled = false;
});
search.addEventListener('input', render); filter.addEventListener('change', render);
refresh().catch(error => { message.textContent = error.message; });
