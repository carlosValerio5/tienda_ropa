import { useState, useEffect } from "react";

interface Producto {
    id: number;
    nombre: string;
    precio: number;
    stock: number;
    talla?: string;
    descripcionCompleta: string;
    genero: string;
    categoria: string;
    color?: string;
    marca?: string;
}

interface Pedido {
    pedido_id: number;
    cliente_nombre: string;
    fecha_pedido: Date;
    estado_pedido: string;
    total: number;
}

interface DetallePedido {
    detalle_id: number;
    pedido_id: number;
    producto_id: number;
    nombre: string;
    cantidad: number;
    precio_unitario: number;
    subtotal: number;
}

const GestionarPedidos = () => {
    const [productos, setProductos] = useState<Producto[]>([]);
    const [carrito, setCarrito] = useState<DetallePedido[]>([]);
    const [pedidos, setPedidos] = useState<Pedido[]>([]);
    const [clienteNombre, setClienteNombre] = useState<string>("");

    // Cargar los productos desde el backend
    useEffect(() => {
        fetch("/api/productos") // Cambiar la URL al endpoint real
            .then((res) => res.json())
            .then((data) => setProductos(data))
            .catch((err) => console.error("Error al cargar productos:", err));
    }, []);

    // Agregar productos al carrito
    const agregarProductoACarrito = (producto: Producto, cantidad: number) => {
        if (cantidad > producto.stock) {
            alert(`No hay suficiente stock para agregar ${producto.nombre}`);
            return;
        }
        const productoExistente = carrito.find((item) => item.producto_id === producto.id);
        if (productoExistente) {
            setCarrito(
                carrito.map((item) =>
                    item.producto_id === producto.id
                        ? {
                            ...item,
                            cantidad: item.cantidad + cantidad,
                            subtotal: (item.cantidad + cantidad) * item.precio_unitario,
                        }
                        : item
                )
            );
        } else {
            setCarrito([
                ...carrito,
                {
                    detalle_id: Date.now(),
                    pedido_id: 0,
                    producto_id: producto.id,
                    nombre: producto.nombre,
                    cantidad,
                    precio_unitario: producto.precio,
                    subtotal: cantidad * producto.precio,
                },
            ]);
        }
    };

    // Confirmar el pedido, enviándolo al backend
    const confirmarPedido = () => {
        if (carrito.length === 0) {
            alert("El carrito está vacío.");
            return;
        }
        if (!clienteNombre.trim()) {
            alert("Por favor, ingrese el nombre del cliente.");
            return;
        }

        const nuevoPedido = {
            cliente_nombre: clienteNombre,
            fecha_pedido: new Date(),
            estado_pedido: "Pendiente",
            total: carrito.reduce((acc, item) => acc + item.subtotal, 0),
            detalles: carrito.map((item) => ({
                producto_id: item.producto_id,
                cantidad: item.cantidad,
                precio_unitario: item.precio_unitario,
            })),
        };

        fetch("/api/pedidos", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(nuevoPedido),
        })
            .then((res) => {
                if (!res.ok) {
                    throw new Error("Error al realizar el pedido.");
                }
                return res.json();
            })
            .then((data) => {
                setPedidos([...pedidos, data]);
                setCarrito([]);
                setClienteNombre("");
                alert("Pedido confirmado exitosamente.");
            })
            .catch((err) => console.error("Error al confirmar pedido:", err));
    };

    // Cancelar un pedido, enviando la solicitud al backend
    const cancelarPedido = (pedido_id: number) => {
        fetch(`/api/pedidos/${pedido_id}`, {
            method: "DELETE",
        })
            .then((res) => {
                if (!res.ok) {
                    throw new Error("Error al cancelar el pedido.");
                }
                setPedidos(pedidos.filter((pedido) => pedido.pedido_id !== pedido_id));
                alert("Pedido cancelado exitosamente.");
            })
            .catch((err) => console.error("Error al cancelar pedido:", err));
    };

    return (
        <div className="p-6 bg-gray-100 min-h-screen">
            <h1 className="text-4xl font-bold text-center mb-8 text-blue-600">
                Gestión de Pedidos
            </h1>

            <div className="bg-white p-6 rounded shadow-md mb-6">
                <h2 className="text-lg font-semibold mb-4">Información del Cliente</h2>
                <label className="block font-semibold text-gray-700 mb-2">Nombre del Cliente:</label>
                <input
                    type="text"
                    value={clienteNombre}
                    onChange={(e) => setClienteNombre(e.target.value)}
                    className="border w-full p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
                    placeholder="Ingrese el nombre del cliente"
                />
            </div>

            <div className="bg-white p-6 rounded shadow-md mb-6">
                <h2 className="text-lg font-semibold mb-4">Productos Disponibles</h2>
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                    {productos.map((producto) => (
                        <div key={producto.id} className="border p-4 rounded shadow hover:shadow-lg bg-gray-50">
                            <h3 className="text-lg font-semibold mb-2">{producto.nombre}</h3>
                            <p className="text-sm text-gray-600 mb-2">{producto.descripcionCompleta}</p>
                            <p className="font-semibold text-gray-800 mb-2">Precio: ${producto.precio}</p>
                            <p className="text-sm text-gray-700 mb-4">Stock Disponible: {producto.stock}</p>
                            <button
                                onClick={() => agregarProductoACarrito(producto, 1)}
                                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700"
                            >
                                Agregar al Carrito
                            </button>
                        </div>
                    ))}
                </div>
            </div>

            <div className="bg-white p-6 rounded shadow-md mb-6">
                <h2 className="text-lg font-semibold mb-4">Carrito</h2>
                {carrito.length > 0 ? (
                    <div>
                        <ul>
                            {carrito.map((item) => (
                                <li key={item.detalle_id} className="flex justify-between items-center mb-2 border-b pb-2">
                                    <span>{item.nombre} (Cantidad: {item.cantidad})</span>
                                    <span className="font-semibold">${item.subtotal}</span>
                                </li>
                            ))}
                        </ul>
                        <button
                            onClick={confirmarPedido}
                            className="mt-4 bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700"
                        >
                            Confirmar Pedido
                        </button>
                    </div>
                ) : (
                    <p className="text-gray-600">El carrito está vacío.</p>
                )}
            </div>

            <div className="bg-white p-6 rounded shadow-md">
                <h2 className="text-lg font-semibold mb-4">Pedidos Realizados</h2>
                {pedidos.length > 0 ? (
                    <ul>
                        {pedidos.map((pedido) => (
                            <li key={pedido.pedido_id} className="flex justify-between items-center mb-2 border-b pb-2">
                                Pedido #{pedido.pedido_id} - Cliente: {pedido.cliente_nombre} - Total: ${pedido.total}
                                <button
                                    onClick={() => cancelarPedido(pedido.pedido_id)}
                                    className="text-red-600 hover:underline"
                                >
                                    Cancelar
                                </button>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p className="text-gray-600">No hay pedidos registrados.</p>
                )}
            </div>
        </div>
    );
};

export default GestionarPedidos;