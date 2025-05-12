import { useState } from 'react';
import { Link } from 'react-router-dom';
import { ChevronDown, ChevronUp } from 'lucide-react';

const Sidebar = () => {
    const [productosOpen, setProductosOpen] = useState(false);

    return (
        <div className="w-64 bg-gray-800 text-white min-h-screen p-4">
            <h2 className="text-xl font-bold mb-6">Tienda</h2>
            <nav className="space-y-2">
                <div>
                    <button
                        onClick={() => setProductosOpen(!productosOpen)}
                        className="w-full flex justify-between items-center p-2 rounded hover:bg-gray-700"
                    >
                        <span>📦 Productos</span>
                        {productosOpen ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                    </button>

                    {productosOpen && (
                        <ul className="ml-4 mt-1 space-y-1 text-sm text-gray-300">
                            <li>
                                <Link to="/productos" className="block py-1 hover:text-white">📋 Lista de productos</Link>
                            </li>
                            <li>
                                <Link to="/productos/nuevo" className="block py-1 hover:text-white">➕ Agregar producto</Link>
                            </li>
                            <li>
                                <Link to="/productos/nuevo" className="block py-1 hover:text-white">➕ Agregar producto</Link>
                            </li>
                        </ul>
                    )}
                </div>

                <Link to="/pedidos" className="block hover:bg-gray-700 p-2 rounded">🛒 Pedidos</Link>
                <Link to="/compras" className="block hover:bg-gray-700 p-2 rounded">🧾 Compras</Link>
                <Link to="/ventas" className="block hover:bg-gray-700 p-2 rounded">💰 Ventas</Link>
            </nav>
        </div>
    );
};

export default Sidebar;
