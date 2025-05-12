import { useState, useEffect } from "react";

interface Producto {
    productoId: number;
    nombre: string;
    precio: number;
    stock: number;
    talla_id?: string;
    color_id?: string;
    marcaNombre?: string;
}

const BuscarProducto = () => {
    const [productos, setProductos] = useState<Producto[]>([]);
    const [busqueda, setBusqueda] = useState("");
    const [filtroTalla, setFiltroTalla] = useState("");
    const [filtroColor, setFiltroColor] = useState("");
    const [filtroMarca, setFiltroMarca] = useState("");
    const [cargando, setCargando] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // Función para cargar datos desde el backend (puede incluir filtros)
    const fetchProductos = async () => {
        setCargando(true);
        setError(null);
        try {
            const queryParams = new URLSearchParams({
                nombre: busqueda,
                talla: filtroTalla,
                color: filtroColor,
                marca: filtroMarca,
            });

            // Llamada al servidor Backend
            const response = await fetch(`http://localhost:8080/api/v1/productos?${queryParams.toString()}`);
            if (!response.ok) {
                throw new Error("Error al cargar los productos.");
            }
            const data: Producto[] = await response.json();
            setProductos(data);
        } catch (e) {
            console.error("Error al obtener los productos:", e);
            setError("No se pudieron cargar los productos. Intente más tarde.");
        } finally {
            setCargando(false);
        }
    };

    // Recargar productos cada vez que cambien filtros o búsqueda
    useEffect(() => {
        fetchProductos();
    }, [busqueda, filtroTalla, filtroColor, filtroMarca]);

    return (
        <div className="p-6 bg-gray-100 min-h-screen">
            <h1 className="text-4xl font-bold text-center mb-8 text-blue-600">
                Buscar Productos
            </h1>

            {/* Filtros de búsqueda */}
            <div className="bg-white p-6 rounded shadow mb-6">
                <h2 className="text-lg font-semibold mb-4">Filtros de Búsqueda</h2>
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
                    <input
                        type="text"
                        placeholder="Buscar por nombre"
                        value={busqueda}
                        onChange={(e) => setBusqueda(e.target.value)}
                        className="border py-2 px-4 rounded focus:ring-2 focus:ring-blue-400 outline-none"
                    />
                    <select
                        value={filtroTalla}
                        onChange={(e) => setFiltroTalla(e.target.value)}
                        className="border py-2 px-4 rounded focus:ring-2 focus:ring-blue-400 outline-none"
                    >
                        <option value="">Selecciona una talla</option>
                        <option value="XS">XS</option>
                        <option value="S">S</option>
                        <option value="M">M</option>
                        <option value="L">L</option>
                        <option value="XL">XL</option>
                        <option value="XXL">XXL</option>
                    </select>
                    <input
                        type="text"
                        placeholder="Filtrar por color"
                        value={filtroColor}
                        onChange={(e) => setFiltroColor(e.target.value)}
                        className="border py-2 px-4 rounded focus:ring-2 focus:ring-blue-400 outline-none"
                    />
                    <input
                        type="text"
                        placeholder="Filtrar por marca"
                        value={filtroMarca}
                        onChange={(e) => setFiltroMarca(e.target.value)}
                        className="border py-2 px-4 rounded focus:ring-2 focus:ring-blue-400 outline-none"
                    />
                </div>
                <div className="text-sm text-gray-500 mt-2">
                    * Puedes combinar varios filtros para obtener resultados más precisos.
                </div>
            </div>


            <div className="bg-white p-6 rounded shadow">
                <h2 className="text-lg font-semibold mb-4">Resultados</h2>
                {cargando ? (
                    <p className="text-gray-600">Cargando productos...</p>
                ) : error ? (
                    <p className="text-red-600">{error}</p>
                ) : productos.length > 0 ? (
                    <table className="w-full border-collapse bg-gray-50 shadow rounded overflow-hidden">
                        <thead className="bg-gray-200">
                        <tr>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">
                                Nombre
                            </th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">
                                Precio (USD)
                            </th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">
                                Stock
                            </th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">
                                Talla
                            </th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">
                                Color
                            </th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">
                                Marca
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        {productos.map((p) => (
                            <tr key={p.productoId} className="border-t bg-white hover:bg-gray-100">
                                <td className="p-4 text-gray-700">{p.nombre}</td>
                                <td className="p-4 text-gray-700">${p.precio}</td>
                                <td className="p-4 text-gray-700">{p.stock}</td>
                                <td className="p-4 text-gray-700">{p.talla_id || "N/A"}</td>
                                <td className="p-4 text-gray-700">{p.color_id || "N/A"}</td>
                                <td className="p-4 text-gray-700">{p.marcaNombre || "N/A"}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p className="text-gray-600">No se encontraron productos.</p>
                )}
            </div>
        </div>
    );
};

export default BuscarProducto;