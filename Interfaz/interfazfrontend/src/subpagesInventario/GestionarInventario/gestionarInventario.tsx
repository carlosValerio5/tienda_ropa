import { useState, useEffect } from "react";

interface Inventario {
    inventarioId: number;
    producto_id: number;
    cantidad: number;
    fecha_actualizacion: string; // Fecha en formato ISO
    producto: {
        nombre: string;
        descripcion: string;
    };
}

const VerInventario = () => {
    const [inventario, setInventario] = useState<Inventario[]>([]);
    const [cargando, setCargando] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    // Cargar datos del inventario desde el backend
    useEffect(() => {
        fetch("http://localhost:8080/api/v1/inventario") // Cambiar por el endpoint correcto
            .then((res) => {
                if (!res.ok) {
                    throw new Error("Error al cargar el inventario.");
                }
                return res.json();
            })
            .then((data) => {
                setInventario(data); // Asignar los datos del inventario
                setCargando(false);
            })
            .catch((err) => {
                console.error("Error al obtener el inventario:", err);
                setError("Error al cargar el inventario. Por favor, intente más tarde.");
                setCargando(false);
            });
    }, []);

    // Función para actualizar el stock en el backend
    const actualizarStock = (inventarioId: number, nuevaCantidad: number) => {
        fetch(`http://localhost:8080/api/v1/inventario/${inventarioId}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ cantidad: nuevaCantidad }),
        })
            .then((res) => {
                if (!res.ok) {
                    throw new Error("Error al actualizar el stock.");
                }
                return res.json();
            })
            .then((data) => {
                // Actualizar el estado del inventario sin recargar
                setInventario((prevInventario) =>
                    prevInventario.map((item) =>
                        item.inventarioId === inventarioId
                            ? { ...item, cantidad: data.cantidad, fecha_actualizacion: data.fecha_actualizacion }
                            : item
                    )
                );
                alert("Stock actualizado correctamente.");
            })
            .catch((err) => {
                console.error("Error al actualizar el stock:", err);
                alert("Error al actualizar el stock. Por favor, intente de nuevo.");
            });
    };

    // Manejar acciones para incrementar o decrementar cantidad
    const manejarActualizarCantidad = (inventarioId: number, cantidadActual: number, accion: "aumentar" | "reducir") => {
        const nuevaCantidad = accion === "aumentar" ? cantidadActual + 1 : cantidadActual - 1;
        if (nuevaCantidad < 0) {
            alert("La cantidad no puede ser negativa.");
            return;
        }
        actualizarStock(inventarioId, nuevaCantidad);
    };

    return (
        <div className="p-6 bg-gray-100 min-h-screen">
            <h1 className="text-4xl font-bold text-center mb-8 text-blue-600">
                Inventario de Productos
            </h1>

            <div className="bg-white p-6 rounded shadow">
                {cargando ? (
                    <p className="text-gray-600">Cargando inventario...</p>
                ) : error ? (
                    <p className="text-red-600">{error}</p>
                ) : inventario.length > 0 ? (
                    <table className="w-full border-collapse bg-gray-50 shadow rounded overflow-hidden">
                        <thead className="bg-gray-200">
                        <tr>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Inventario ID</th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Producto ID</th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Nombre del Producto</th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Descripción</th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Cantidad</th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Última Actualización</th>
                            <th className="p-4 text-left text-sm font-semibold text-gray-600">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        {inventario.map((item) => (
                            <tr key={item.inventarioId} className="border-t bg-white hover:bg-gray-100">
                                <td className="p-4 text-gray-700">{item.inventarioId}</td>
                                <td className="p-4 text-gray-700">{item.producto_id}</td>
                                <td className="p-4 text-gray-700">{item.producto.nombre}</td>
                                <td className="p-4 text-gray-700">{item.producto.descripcion}</td>
                                <td className="p-4 text-gray-700">{item.cantidad}</td>
                                <td className="p-4 text-gray-700">
                                    {new Date(item.fecha_actualizacion).toLocaleDateString()}
                                </td>
                                <td className="p-4 text-gray-700 flex items-center gap-4">
                                    <button
                                        onClick={() => manejarActualizarCantidad(item.inventarioId, item.cantidad, "aumentar")}
                                        className="bg-green-500 text-white px-2 py-1 rounded hover:bg-green-700"
                                    >
                                        +1
                                    </button>
                                    <button
                                        onClick={() => manejarActualizarCantidad(item.inventarioId, item.cantidad, "reducir")}
                                        className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-700"
                                    >
                                        -1
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                ) : (
                    <p className="text-gray-600">No hay datos de inventario disponibles.</p>
                )}
            </div>
        </div>
    );
};

export default VerInventario;