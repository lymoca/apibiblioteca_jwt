const API_URL = 'http://localhost:8080/api/libros';

document.addEventListener('DOMContentLoaded', cargarTodos);

function cargarTodos() {
    document.getElementById('busquedaTitulo').value = '';
    fetch(`${API_URL}?page=0&size=20`) // AJAX con FETCH
        .then(response => {
            if (!response.ok) throw new Error('Error API');
            return response.json();
        })
        .then(data => mostrarLibros(data.content))
        .catch(error => console.error('Error:', error));
}

function buscarPorTitulo() {
    const texto = document.getElementById('busquedaTitulo').value;
    if (!texto.trim()) return cargarTodos();

    fetch(`${API_URL}?titulo=${texto}`)
        .then(response => response.json())
        .then(data => mostrarLibros(data))
        .catch(error => console.error('Error:', error));
}

function borrarLibro(id) {
    if (!confirm(`¿Borrar libro ID: ${id}?`)) return;
    fetch(`${API_URL}/${id}`, { method: 'DELETE' })
    .then(response => {
        if (response.ok) { alert('Eliminado'); cargarTodos(); }
        else { alert('Error al eliminar'); }
    })
    .catch(error => console.error('Error de red/CORS:', error));
}

function mostrarLibros(lista) {
    const tbody = document.getElementById('tablaCuerpo');
    tbody.innerHTML = '';
    if (!lista || lista.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center">No hay libros</td></tr>';
        return;
    }
    lista.forEach(libro => {
        tbody.innerHTML += `
            <tr>
                <td>${libro.id}</td>
                <td>${libro.titulo}</td>
                <td>${libro.isbn}</td>
                <td>${libro.fechaPublicacion}</td>
                <td class="text-center">
                    <button class="btn btn-danger btn-sm" onclick="borrarLibro(${libro.id})">🗑 Eliminar</button>
                </td>
            </tr>`;
    });
}