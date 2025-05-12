import { useState, useEffect } from 'react';

interface Resena {
    resena_id: number;
    producto_id: number;
    cliente_id: number;
    calificacion: number;
    comentario: string;
    fecha: string;
}

const GestionarResenas = () => {
    const [resenas, setResenas] = useState<Resena[]>([]);
    const [productos, setProductos] = useState<{ id: number; nombre: string }[]>([]); // Lista de productos
    const [clientes, setClientes] = useState<{ id: number; nombre: string }[]>([]); // Lista de clientes

    const [productoId, setProductoId] = useState<number | ''>('');
    const [clienteId, setClienteId] = useState<number | ''>('');
    const [calificacion, setCalificacion] = useState<number>(5);
    const [comentario, setComentario] = useState('');
    const [formError, setFormError] = useState<string | null>(null);
    const [editando, setEditando] = useState<number | null>(null);
    const [cargando, setCargando] = useState(false);

    const API_URL = '/api/resenas';

    const fetchResenas = async () => {
        try {
            setCargando(true);
            const response = await fetch(API_URL);
            if (!response.ok) {
                throw new Error('Error al obtener las resenas.');
            }
            const data: Resena[] = await response.json();
            setResenas(data);
        } catch {
            setFormError('No se pudieron cargar las resenas.');
        } finally {
            setCargando(false);
        }
    };

    const fetchProductosYClientes = async () => {
        try {
            const resProductos = await fetch('/api/productos');
            const resClientes = await fetch('/api/clientes');

            const productosData = await resProductos.json();
            const clientesData = await resClientes.json();

            setProductos(productosData);
            setClientes(clientesData);
        } catch {
            setFormError('No se pudieron cargar productos o clientes.');
        }
    };

    const agregarResena = async () => {
        if (productoId === '' || clienteId === '' || calificacion < 1 || calificacion > 5 || !comentario.trim()) {
            setFormError('Todos los campos son obligatorios.');
            return;
        }
        setCargando(true);
        setFormError(null);

        try {
            const nuevaResena = { producto_id: productoId, cliente_id: clienteId, calificacion, comentario };

            const response = await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(nuevaResena),
            });

            if (!response.ok) {
                throw new Error('Error al agregar la resena.');
            }

            const resenaCreada: Resena = await response.json();
            setResenas([...resenas, resenaCreada]);
            limpiarFormulario();
        } catch {
            setFormError('No se pudo agregar la resena.');
        } finally {
            setCargando(false);
        }
    };

    const editarResena = async () => {
        if (editando === null) return;
        if (productoId === '' || clienteId === '' || calificacion < 1 || calificacion > 5 || !comentario.trim()) {
            setFormError('Todos los campos son obligatorios.');
            return;
        }

        setCargando(true);
        setFormError(null);

        try {
            const resenaActualizada = { producto_id: productoId, cliente_id: clienteId, calificacion, comentario };

            const response = await fetch(`${API_URL}/${editando}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(resenaActualizada),
            });

            if (!response.ok) {
                throw new Error('Error al guardar los cambios.');
            }

            const resenaEditada: Resena = await response.json();
            setResenas(resenas.map((r) => (r.resena_id === editando ? resenaEditada : r)));
            limpiarFormulario();
        } catch {
            setFormError('No se pudieron guardar los cambios.');
        } finally {
            setCargando(false);
        }
    };

    const eliminarResena = async (id: number) => {
        setCargando(true);
        setFormError(null);

        try {
            const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
            if (!response.ok) {
                throw new Error('Error al eliminar la resena.');
            }
            setResenas(resenas.filter((r) => r.resena_id !== id));
        } catch {
            setFormError('No se pudo eliminar la resena.');
        } finally {
            setCargando(false);
        }
    };

    const cargarEdicion = (resena: Resena) => {
        setProductoId(resena.producto_id);
        setClienteId(resena.cliente_id);
        setCalificacion(resena.calificacion);
        setComentario(resena.comentario);
        setEditando(resena.resena_id);
    };

    const limpiarFormulario = () => {
        setProductoId('');
        setClienteId('');
        setCalificacion(5);
        setComentario('');
        setEditando(null);
    };

    useEffect(() => {
        fetchResenas();
        fetchProductosYClientes();
    }, []);

    return (
        <div>
            {cargando && <p>Cargando...</p>}
            {formError && <p className="text-red-500">{formError}</p>}

            <h1 className="text-4xl font-bold text-center mb-8 text-blue-600">Gestionar Resenas</h1>

            {/* Formulario */}
            <div className="p-4 bg-white shadow rounded mb-6">
                <h2 className="text-lg font-semibold mb-2">{editando ? 'Editar Resena' : 'Agregar Nueva Resena'}</h2>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                    <select value={productoId} onChange={(e) => setProductoId(Number(e.target.value))} className="border p-2 rounded">
                        <option value="">Selecciona un producto</option>
                        {productos.map((p) => (
                            <option key={p.id} value={p.id}>
                                {p.nombre}
                            </option>
                        ))}
                    </select>
                    <select value={clienteId} onChange={(e) => setClienteId(Number(e.target.value))} className="border p-2 rounded">
                        <option value="">Selecciona un cliente</option>
                        {clientes.map((c) => (
                            <option key={c.id} value={c.id}>
                                {c.nombre}
                            </option>
                        ))}
                    </select>
                    <input
                        type="number"
                        placeholder="Calificacion (1-5)"
                        value={calificacion}
                        onChange={(e) => setCalificacion(Number(e.target.value))}
                        className="border p-2 rounded"
                    />
                    <textarea
                        placeholder="Comentario"
                        value={comentario}
                        onChange={(e) => setComentario(e.target.value)}
                        className="border p-2 rounded resize-none"
                    />
                </div>
                <div className="mt-4">
                    {editando ? (
                        <button onClick={editarResena} className="bg-yellow-500 text-white px-4 py-2 rounded">
                            Guardar Cambios
                        </button>
                    ) : (
                        <button onClick={agregarResena} className="bg-green-600 text-white px-4 py-2 rounded">
                            Agregar Resena
                        </button>
                    )}
                    <button onClick={limpiarFormulario} className="bg-gray-400 text-white px-4 py-2 rounded">
                        Limpiar
                    </button>
                </div>
            </div>

            {/* Tabla */}
            <div className="bg-white shadow rounded">
                <h2 className="text-lg font-semibold p-4 border-b">Lista de Resenas</h2>
                <table className="w-full table-auto">
                    <thead>
                    <tr className="bg-gray-100">
                        <th className="p-2">Producto</th>
                        <th className="p-2">Cliente</th>
                        <th className="p-2">Calificacion</th>
                        <th className="p-2">Comentario</th>
                        <th className="p-2">Fecha</th>
                        <th className="p-2">Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    {resenas.map((r) => (
                        <tr key={r.resena_id} className="border-t">
                            <td className="p-2">{productos.find((p) => p.id === r.producto_id)?.nombre || 'N/A'}</td>
                            <td className="p-2">{clientes.find((c) => c.id === r.cliente_id)?.nombre || 'N/A'}</td>
                            <td className="p-2">{r.calificacion}</td>
                            <td className="p-2 max-w-[200px] truncate">{r.comentario}</td>
                            <td className="p-2">{r.fecha}</td>
                            <td className="p-2">
                                <button onClick={() => cargarEdicion(r)} className="bg-blue-500 text-white px-3 py-1 rounded">
                                    Editar
                                </button>
                                <button onClick={() => eliminarResena(r.resena_id)} className="bg-red-500 text-white px-3 py-1 rounded">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    ))}
                    {resenas.length === 0 && (
                        <tr>
                            <td colSpan={6} className="p-4">
                                No se encontraron resenas.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default GestionarResenas;