import {useEffect, useState} from 'react';

interface Cliente {
    cliente_id: number;
    nombre: string;
    apellido: string;
    email: string;
    telefono: string;
    direccion: string;
    fecha_registro: number;
}

const GestionarClientes = () => {
    const [clientes, setClientes] = useState<Cliente[]>([]);
    const [nombre, setNombre] = useState('');
    const [apellido, setApellido] = useState('');
    const [email, setEmail] = useState('');
    const [telefono, setTelefono] = useState('');
    const [direccion, setDireccion] = useState('');
    const [formError, setFormError] = useState<string | null>(null);
    const [editando, setEditando] = useState<number | null>(null);
    const [cargando, setCargando] = useState(false);
    const [clienteId, setClienteId] = useState(0);


    // Cambia esta URL si tu backend está en una ruta diferente (ejemplo: 'http://localhost:5000/api/clientes')
    const API_URL = 'http://localhost:8080/api/v1/clientes';

    const fetchClientes = async () => {
        setCargando(true);
        setFormError(null);
        try {
            const response = await fetch(API_URL);
            if (!response.ok) {
                throw new Error('Error al obtener los clientes.');
            }
            const data: Cliente[] = await response.json();
            setClientes(data);
        } catch {
            setFormError('No se pudieron cargar los clientes.');
        } finally {
            setCargando(false);
        }
    };

    const agregarCliente = async () => {
        if (!nombre.trim() || !apellido.trim() || !email.trim() || !telefono.trim() || !direccion.trim()) {
            setFormError('Todos los campos son obligatorios.');
            return;
        }

        setCargando(true);
        setFormError(null);

        const nuevoCliente = { nombre, apellido, email, telefono, direccion };

        try {
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(nuevoCliente),
            });

            if (!response.ok) {
                throw new Error('Error al agregar el cliente.');
            }

            const clienteCreado: Cliente = await response.json();
            setClientes([...clientes, clienteCreado]);
            limpiarFormulario();
            fetchClientes();
        } catch {
            setFormError('No se pudo agregar el cliente.');
        } finally {
            setCargando(false);
        }
    };

    const editarCliente = async () => {
        if (editando === null) return;

        if (clienteId == null || clienteId == 0 || !nombre.trim() || !apellido.trim() ||
            !email.trim() || !telefono.trim() || !direccion.trim()) {
            setFormError('Todos los campos son obligatorios.');
            return;
        }

        setCargando(true);
        setFormError(null);


        const clienteActualizado = { cliente_id:clienteId, nombre, apellido, email, telefono, direccion };


        console.log(clienteActualizado);
        try {
            const response = await fetch(`${API_URL}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(clienteActualizado),
            });

            if (!response.ok) {
                throw new Error('Error al guardar los cambios.');
            }

            const clienteEditado: Cliente = await response.json();
            setClientes(clientes.map((c) => (c.cliente_id === editando ? clienteEditado : c)));
            limpiarFormulario();
        } catch {
            setFormError('No se pudieron guardar los cambios.');
        } finally {
            setCargando(false);
        }
    };

    const eliminarCliente = async (id: number) => {
        setCargando(true);
        setFormError(null);

        try {
            const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
            if (!response.ok) {
                throw new Error('Error al eliminar el cliente.');
            }
            fetchClientes();
        } catch {
            setFormError('No se pudo eliminar el cliente.');
        } finally {
            setCargando(false);
        }
    };

    const cargarEdicion = (cliente: Cliente) => {
        setClienteId(cliente.cliente_id)
        setNombre(cliente.nombre);
        setApellido(cliente.apellido);
        setEmail(cliente.email);
        setTelefono(cliente.telefono);
        setDireccion(cliente.direccion);
        setEditando(cliente.cliente_id);
    };

    const limpiarFormulario = () => {
        setNombre('');
        setApellido('');
        setEmail('');
        setTelefono('');
        setDireccion('');
        setEditando(null);
    };

    useEffect(() => {
        fetchClientes();
    }, []);

    return (
        <div>
            {cargando && <p>Cargando...</p>}
            {formError && <p className="text-red-500">{formError}</p>}

            <h1 className="text-4xl font-bold text-center mb-8 text-blue-600">Gestionar Clientes</h1>

            {/* Formulario */}
            <div className="p-4 bg-white shadow rounded mb-6">
                <h2 className="text-lg font-semibold mb-2">{editando ? 'Editar Cliente' : 'Agregar Nuevo Cliente'}</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <input
                        type="text"
                        placeholder="Nombre"
                        value={nombre}
                        onChange={(e) => setNombre(e.target.value)}
                        className="border p-2 rounded"
                    />
                    <input
                        type="text"
                        placeholder="Apellido"
                        value={apellido}
                        onChange={(e) => setApellido(e.target.value)}
                        className="border p-2 rounded"
                    />
                    <input
                        type="email"
                        placeholder="Correo Electrónico"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="border p-2 rounded"
                    />
                    <input
                        type="text"
                        placeholder="Teléfono"
                        value={telefono}
                        onChange={(e) => setTelefono(e.target.value)}
                        className="border p-2 rounded"
                    />
                    <textarea
                        placeholder="Dirección"
                        value={direccion}
                        onChange={(e) => setDireccion(e.target.value)}
                        className="border p-2 rounded resize-none"
                    />
                </div>
                <div className="mt-4">
                    {editando ? (
                        <button onClick={editarCliente} className="bg-yellow-500 text-white px-4 py-2 rounded">
                            Guardar Cambios
                        </button>
                    ) : (
                        <button onClick={agregarCliente} className="bg-green-600 text-white px-4 py-2 rounded">
                            Agregar Cliente
                        </button>
                    )}
                    <button onClick={limpiarFormulario} className="bg-gray-400 text-white px-4 py-2 rounded">
                        Limpiar
                    </button>
                </div>
            </div>

            {/* Tabla */}
            <div className="bg-white shadow rounded">
                <h2 className="text-lg font-semibold p-4 border-b">Lista de Clientes</h2>
                <table className="w-full table-auto">
                    <thead>
                    <tr className="bg-gray-100">
                        <th className="p-2">Nombre</th>
                        <th className="p-2">Apellido</th>
                        <th className="p-2">Correo</th>
                        <th className="p-2">Teléfono</th>
                        <th className="p-2">Dirección</th>
                        <th className="p-2">Fecha Registro</th>
                        <th className="p-2">Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    {clientes.map((c) => (
                        <tr key={c.cliente_id} className="border-t">
                            <td className="p-2">{c.nombre}</td>
                            <td className="p-2">{c.apellido}</td>
                            <td className="p-2">{c.email}</td>
                            <td className="p-2">{c.telefono}</td>
                            <td className="p-2">{c.direccion}</td>
                            <td className="p-2">{c.fecha_registro}</td>
                            <td className="p-2 flex items-center justify-center">
                                <button onClick={() => cargarEdicion(c)} className="bg-blue-500 text-white px-3 py-1 rounded ">
                                    Editar
                                </button>
                                <button onClick={() => eliminarCliente(c.cliente_id)} className="bg-red-500 text-white px-3 py-1 rounded">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    ))}
                    {clientes.length === 0 && (
                        <tr>
                            <td colSpan={7} className="p-4">
                                No se encontraron clientes.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default GestionarClientes;