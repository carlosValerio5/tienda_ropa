import { useState } from 'react';

interface Cliente {
    cliente_id: number;
    nombre: string;
    apellido: string;
    email: string;
    telefono: string;
    direccion: string;
    fecha_registro: string;
}

const BuscarCliente = () => {
    const [criterio, setCriterio] = useState('');
    const [clientes, setClientes] = useState<Cliente[]>([]);
    const [cargando, setCargando] = useState(false);
    const [formError, setFormError] = useState<string | null>(null);

    // Cambia esta URL si tu backend tiene un prefijo diferente (ejemplo: '/api/v1/clientes')
    const API_URL = 'http://localhost:8080/api/v1/clientes';

    const buscarClientes = async () => {
        if (!criterio.trim()) {
            setFormError('Por favor, ingresa un criterio de búsqueda.');
            return;
        }

        setCargando(true);
        setFormError(null);

        try {
            const response = await fetch(`${API_URL}/search?search=${encodeURIComponent(criterio)}`);
            if (!response.ok) {
                throw new Error('Error al buscar clientes.');
            }
            const data: Cliente[] = await response.json();
            setClientes(data);

            if (data.length === 0) {
                setFormError('No se encontraron clientes con el criterio indicado.');
            }
        } catch (e) {
            console.error(e);
            setFormError('Ocurrió un error al realizar la búsqueda.');
        } finally {
            setCargando(false);
        }
    };

    return (
        <div>
            <h1 className="text-4xl font-bold text-center mb-8 text-blue-600">Buscar Clientes</h1>

            {/* Campo de Búsqueda */}
            <div className="mb-6">
                <input
                    type="text"
                    placeholder="Busca por nombre, apellido, correo, etc."
                    value={criterio}
                    onChange={(e) => setCriterio(e.target.value)}
                    className="border p-2 rounded w-full md:w-1/2 mb-4"
                />
                <button
                    onClick={buscarClientes}
                    className="bg-green-600 text-white px-4 py-2 rounded"
                >
                    Buscar
                </button>
            </div>

            {/* Mensajes de Cargando/Error */}
            {cargando && <p className="text-blue-500">Buscando clientes...</p>}
            {formError && <p className="text-red-500 mb-4">{formError}</p>}

            {/* Resultados de la Búsqueda */}
            <div className="bg-white shadow rounded">
                <h2 className="text-lg font-semibold p-4 border-b">Resultados de la Búsqueda</h2>
                <table className="w-full table-auto">
                    <thead>
                    <tr className="bg-gray-100">
                        <th className="p-2">Nombre</th>
                        <th className="p-2">Apellido</th>
                        <th className="p-2">Correo</th>
                        <th className="p-2">Teléfono</th>
                        <th className="p-2">Dirección</th>
                        <th className="p-2">Fecha Registro</th>
                    </tr>
                    </thead>
                    <tbody>
                    {clientes.map((cliente) => (
                        <tr key={cliente.cliente_id} className="border-t">
                            <td className="p-2">{cliente.nombre}</td>
                            <td className="p-2">{cliente.apellido}</td>
                            <td className="p-2">{cliente.email}</td>
                            <td className="p-2">{cliente.telefono}</td>
                            <td className="p-2">{cliente.direccion}</td>
                            <td className="p-2">{cliente.fecha_registro}</td>
                        </tr>
                    ))}
                    {clientes.length === 0 && !cargando && (
                        <tr>
                            <td colSpan={6} className="p-4 text-center">
                                No se encontraron resultados.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default BuscarCliente;