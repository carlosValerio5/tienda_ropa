import { useState } from 'react';
import { Link } from 'react-router-dom';
import { ChevronDown, ChevronUp } from 'lucide-react';

const Sidebar = () => {
    const [productosOpen, setProductosOpen] = useState(false);
    const [pedidosOpen, setPedidosOpen] = useState(false);
    const [inventarioOpen, setInventario] = useState(false);
    const [reseniasOpen, setReseniaOpen] = useState(false);
    const [clientesOpen, setClientes] = useState(false);


    return (
        <aside className="w-64 bg-gray-800 text-white p-4 min-h-screen">
            <h2 className="text-xl font-bold mb-6">Mi Tienda</h2>
            <nav className="space-y-2">
                {/* Submenú para Productos */}
                <div>
                    <button
                        onClick={() => setProductosOpen(!productosOpen)}
                        className="w-full flex justify-between items-center hover:text-yellow-300"
                    >
                        <span>🛍️ Productos</span>
                        {productosOpen ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                    </button>

                    {productosOpen && (
                        <ul className="ml-4 mt-1 text-sm text-gray-300 space-y-1">
                            <li>
                                <Link to="/productos/agregar" className="block hover:text-yellow-300">
                                    📋 Gestion de productos
                                </Link>
                            </li>
                            <li>
                                <Link to="/productos/Index" className="block hover:text-yellow-300">
                                    🏷️ Buscar Productos
                                </Link>
                            </li>
                        </ul>
                    )}
                </div>

                <div>
                    <button
                        onClick={() => setPedidosOpen(!pedidosOpen)}
                        className="w-full flex justify-between items-center hover:text-yellow-300"
                    >
                        <span>📦 Pedidos</span>
                        {pedidosOpen ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                    </button>

                    {pedidosOpen && (
                        <ul className="ml-4 mt-1 text-sm text-gray-300 space-y-1">
                            <li>
                                <Link to="/pedidos/gestionar" className="block hover:text-yellow-300">
                                    📋 Gestion de pedidos
                                </Link>
                            </li>
                            <li>
                                <Link to="/pedidos/listar" className="block hover:text-yellow-300">
                                    🏷️ Buscar pedidos
                                </Link>
                            </li>
                        </ul>
                    )}
                </div>

                <div>
                    <button
                        onClick={() => setInventario(!inventarioOpen)}
                        className="w-full flex justify-between items-center hover:text-yellow-300"
                    >
                        <span>🧾 Inventario</span>
                        {inventarioOpen ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                    </button>

                    {inventarioOpen && (
                        <ul className="ml-4 mt-1 text-sm text-gray-300 space-y-1">
                            <li>
                                <Link to="/inventario/ver" className="block hover:text-yellow-300">
                                    📋 Ver inventario
                                </Link>
                            </li>
                        </ul>
                    )}
                </div>

                <div>
                    <button
                        onClick={() => setClientes(!clientesOpen)}
                        className="w-full flex justify-between items-center hover:text-yellow-300"
                    >
                        <span>👥 Clientes</span>
                        {clientesOpen ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                    </button>

                    {clientesOpen && (
                        <ul className="ml-4 mt-1 text-sm text-gray-300 space-y-1">
                            <li>
                                <Link to="/clientes/registrar" className="block hover:text-yellow-300">
                                    ➕ Registrar Clientes
                                </Link>
                            </li>
                            <li>
                                <Link to="/clientes/buscar" className="block hover:text-yellow-300">
                                    📋 Buscar Clientes
                                </Link>
                            </li>
                        </ul>
                    )}
                </div>


                <div>
                    <button
                        onClick={() => setReseniaOpen(!reseniasOpen)}
                        className="w-full flex justify-between items-center hover:text-yellow-300"
                    >
                        <span>⭐ Reseñas</span>
                        {reseniasOpen ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                    </button>

                    {reseniasOpen && (
                        <ul className="ml-4 mt-1 text-sm text-gray-300 space-y-1">
                            <li>
                                <Link to="/resenias/mostrar" className="block hover:text-yellow-300">
                                    ⭐ Vista de resenas
                                </Link>
                            </li>
                        </ul>
                    )}
                </div>




            </nav>
        </aside>
    );
};

export default Sidebar;
