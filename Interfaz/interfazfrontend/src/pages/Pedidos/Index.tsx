import { useState } from 'react';

interface Pedidos {
    id: number;
    producto: string;
    cantidad: number;
    proveedor: string;
}

const Pedidos = () => {
    const [pedidos, setPedidos] = useState<Pedidos[]>([]);
    const [producto, setProducto] = useState('');
    const [cantidad, setCantidad] = useState(1);
    const [proveedor, setProveedor] = useState('');

    const registrarPedido = () => {
        setPedidos([
            ...pedidos,
            { id: Date.now(), producto, cantidad, proveedor }
        ]);
        setProducto('');
        setCantidad(1);
        setProveedor('');
    };

    return (
        <div>
            <h1 className="text-2xl font-bold mb-4">Registrar Compra</h1>

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
                    placeholder="Proveedor"
                    value={proveedor}
                    onChange={e => setProveedor(e.target.value)}
                    className="border p-2 mr-2 rounded"
                />
                <button
                    onClick={registrarPedido}
                    className="bg-green-600 text-white px-4 py-2 rounded"
                >
                    Registrar
                </button>
            </div>

            <div className="bg-white shadow rounded">
                <h2 className="text-lg font-semibold p-4 border-b">Historial de Compras</h2>
                <table className="w-full table-auto">
                    <thead className="bg-gray-100">
                    <tr>
                        <th className="p-2">Producto</th>
                        <th className="p-2">Cantidad</th>
                        <th className="p-2">Proveedor</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pedidos.map(c => (
                        <tr key={c.id} className="border-t">
                            <td className="p-2">{c.producto}</td>
                            <td className="p-2">{c.cantidad}</td>
                            <td className="p-2">{c.proveedor}</td>
                        </tr>
                    ))}
                    {pedidos.length === 0 && (
                        <tr><td className="p-2" colSpan={3}>No hay compras registradas.</td></tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Pedidos;
