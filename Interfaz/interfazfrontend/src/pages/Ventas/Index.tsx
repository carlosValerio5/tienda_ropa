import { useState } from 'react';

interface Venta {
    id: number;
    producto: string;
    cantidad: number;
    cliente: string;
}

const Ventas = () => {
    const [ventas, setVentas] = useState<Venta[]>([]);
    const [producto, setProducto] = useState('');
    const [cantidad, setCantidad] = useState(1);
    const [cliente, setCliente] = useState('');

    const registrarVenta = () => {
        setVentas([
            ...ventas,
            { id: Date.now(), producto, cantidad, cliente }
        ]);
        setProducto('');
        setCantidad(1);
        setCliente('');
    };

    return (
        <div>
            <h1 className="text-2xl font-bold mb-4">Registrar Venta</h1>

            <div className="bg-white shadow p-4 rounded mb-6">
                <input
                    type="text"
                    placeholder="Producto"
                    value={producto}
                    onChange={e => setProducto(e.target.value)}
                    className="border p-2 mr-2 rounded"
                />
                <input
                    type="number"
                    placeholder="Cantidad"
                    value={cantidad}
                    onChange={e => setCantidad(Number(e.target.value))}
                    className="border p-2 mr-2 rounded"
                />
                <input
                    type="text"
                    placeholder="Cliente"
                    value={cliente}
                    onChange={e => setCliente(e.target.value)}
                    className="border p-2 mr-2 rounded"
                />
                <button
                    onClick={registrarVenta}
                    className="bg-green-600 text-white px-4 py-2 rounded"
                >
                    Registrar
                </button>
            </div>

            <div className="bg-white shadow rounded">
                <h2 className="text-lg font-semibold p-4 border-b">Historial de Ventas</h2>
                <table className="w-full table-auto">
                    <thead className="bg-gray-100">
                    <tr>
                        <th className="p-2">Producto</th>
                        <th className="p-2">Cantidad</th>
                        <th className="p-2">Cliente</th>
                    </tr>
                    </thead>
                    <tbody>
                    {ventas.map(v => (
                        <tr key={v.id} className="border-t">
                            <td className="p-2">{v.producto}</td>
                            <td className="p-2">{v.cantidad}</td>
                            <td className="p-2">{v.cliente}</td>
                        </tr>
                    ))}
                    {ventas.length === 0 && (
                        <tr><td className="p-2" colSpan={3}>No hay ventas registradas.</td></tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Ventas;
