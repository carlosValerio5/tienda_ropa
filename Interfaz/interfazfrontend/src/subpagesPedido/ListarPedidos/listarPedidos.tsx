import { useState } from "react";

interface Pedido {
    pedidoId: number;
    cliente_nombre: string;
    fecha_pedido: string; // Fecha en formato ISO
    estado_pedido: string; // Ejemplo: Pendiente, Completado, Cancelado
    total: number;
    cliente: Cliente;
}

interface Cliente {
    cliente_id: number;
    nombre: string;
    apellido: string;
    email: string;
    telefono: string;
    direccion: string;
}

const BuscarPedidos = () => {
    const [busqueda, setBusqueda] = useState<string>("");
    const [estadoFiltro, setEstadoFiltro] = useState<string>("");
    const [pedidos, setPedidos] = useState<Pedido[]>([]);
    const [cargando, setCargando] = useState<boolean>(false);

    const buscarPedidos = () => {
        if (!busqueda.trim() && !estadoFiltro) {
            alert("Por favor, ingrese un criterio de búsqueda o seleccione un estado.");
            return;
        }

        setCargando(true);

        console.log(estadoFiltro);

        fetch(`http://localhost:8080/api/v1/pedidos/params?clienteNombre=${busqueda}&estado=${estadoFiltro}`) // Reemplazar con la URL del backend
            .then((res) => {
                if (!res.ok) {
                    throw new Error("Error al buscar pedidos.");
                }
                return res.json();
            })
            .then((data) => {
                const updatedPedidos: Pedido[] = data.map(item =>({
                    ...item,
                    cliente_nombre: item.cliente?.nombre||null,
                }));
                setPedidos(updatedPedidos); // Asignar los pedidos obtenidos desde el backend
                setCargando(false);
            })
            .catch((err) => {
                console.error("Error durante la búsqueda:", err);
                setCargando(false);
            });
    };

    return (
        <div className="p-6 bg-gray-100 min-h-screen">
            <h1 className="text-4xl font-bold text-center mb-8 text-blue-600">
                Buscar Pedidos
            </h1>

            <div className="bg-white p-6 rounded shadow mb-6">
                <h2 className="text-lg font-semibold mb-4">Criterios de Búsqueda</h2>
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                    <input
                        type="text"
                        placeholder="Buscar por nombre de Cliente"
                        value={busqueda}
                        onChange={(e) => setBusqueda(e.target.value)}
                        className="border p-2 rounded focus:ring-2 focus:ring-blue-400 outline-none"
                    />
                    <select
                        value={estadoFiltro}
                        onChange={(e) => setEstadoFiltro(e.target.value)}
                        className="border p-2 rounded focus:ring-2 focus:ring-blue-400 outline-none"
                    >
                        <option value="">Todos los Estados</option>
                        <option value="Pendiente">Pendiente</option>
                        <option value="Completado">Completado</option>
                        <option value="Cancelado">Cancelado</option>
                    </select>
                    <button
                        onClick={buscarPedidos}
                        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700"
                    >
                        Buscar
                    </button>
                </div>
            </div>

            <div className="bg-white p-6 rounded shadow">
                <h2 className="text-lg font-semibold mb-4">Resultados</h2>
                {cargando ? (
                    <p className="text-gray-600">Cargando pedidos...</p>
                ) : pedidos.length > 0 ? (
                    <table className="w-full border-collapse bg-gray-50 shadow rounded overflow-hidden">
                        <thead className="bg-gray-200">
                        <tr>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Pedido ID</th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Cliente</th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Fecha</th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Estado</th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Total</th>
                        </tr>
                        </thead>
                        <tbody>
                        {pedidos.map((pedido) => (
                            <tr key={pedido.pedidoId} className="border-t bg-white hover:bg-gray-100">
                                <td className="p-4 text-gray-700">{pedido.pedidoId}</td>
                                <td className="p-4 text-gray-700">{pedido.cliente_nombre}</td>
                                <td className="p-4 text-gray-700">
                                    {new Date(pedido.fecha_pedido).toLocaleDateString()}
                                </td>
                                <td className="p-4 text-gray-700">{pedido.estado_pedido}</td>
                                <td className="p-4 text-gray-700">${pedido.total?pedido.total.toFixed(2):0}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p className="text-gray-600">No se encontraron pedidos con los criterios proporcionados.</p>
                )}
            </div>
        </div>
    );
};

export default BuscarPedidos;