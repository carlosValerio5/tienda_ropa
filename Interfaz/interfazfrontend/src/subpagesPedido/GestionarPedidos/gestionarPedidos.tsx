import {useEffect, useState} from "react";

interface Producto {
    productoId: number;
    nombre: string;
    precio: number;
    stock: number;
    tallaId?: string;
    descripcion: string;
    genero: string;
    categoriaNombre: string;
    color_id?: string;
    marcaNombre?: string;
}

interface PedidoDTO {
    pClienteId: number;
    pEstado: string;
    pProductos: Array<number>;
    pCantidades: Array<number>;
}

interface Pedido {
    pedidoId: number;
    clienteNombre: string;
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

interface Cliente {
    cliente_id: number;
    nombre: string;
    apellido: number;
    email: string;
    telefono: string;
    direccion: string;
}

const GestionarPedidos = () => {
    const [productos, setProductos] = useState<Producto[]>([]);
    const [carrito, setCarrito] = useState<DetallePedido[]>([]);
    const [pedidos, setPedidos] = useState<Pedido[]>([]);
    const [clienteNombre, setClienteNombre] = useState<string>("");

    const getCliente = async (name: string) => {
        try{
            const cliente = await fetch(`http://localhost:8080/api/v1/clientes?name=${name}`);
            console.log(cliente);
            return await cliente.json() as Cliente;
        }catch(error){
            console.log(error);
        }
    }

    const fetchPedidos = async () => {
        try{
            const result = await fetch("http://localhost:8080/api/v1/pedidos");
            const data = await result.json()
            setPedidos(data)
        }catch (e){
            console.log(e);
        }
    }

    useEffect(()=>{
        fetchPedidos();
    }, [])

    // Cargar los productos desde el backend
    useEffect(() => {
        fetch("http://localhost:8080/api/v1/productos") // Cambiar la URL al endpoint real
            .then((res) => res.json())
            .then((data) => setProductos(data))
            .catch((err) => console.error("Error al cargar productos:", err));
    }, [pedidos, carrito]);

    // Agregar productos al carrito
    const agregarProductoACarrito = (producto: Producto, cantidad: number) => {
        if (cantidad > producto.stock) {
            alert(`No hay suficiente stock para agregar ${producto.nombre}`);
            return;
        }
        const productoExistente = carrito.find((item) => item.producto_id === producto.productoId);
        if (productoExistente) {
            setCarrito(
                carrito.map((item) =>
                    item.producto_id === producto.productoId
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
                    producto_id: producto.productoId,
                    nombre: producto.nombre,
                    cantidad,
                    precio_unitario: producto.precio,
                    subtotal: cantidad * producto.precio,
                },
            ]);
        }
    };

    // Confirmar el pedido, enviándolo al backend
    const confirmarPedido = async () => {
        if (carrito.length === 0) {
            alert("El carrito está vacío.");
            return;
        }
        if (!clienteNombre.trim()) {
            alert("Por favor, ingrese el nombre del cliente.");
            return;
        }

        console.log(clienteNombre);

        const cliente: Cliente | undefined = (await getCliente(clienteNombre))[0];

        if (!cliente) return;

        console.log(cliente);//here it is defined

        const cantidad : number[] = carrito.map((item) => item.cantidad);
        const productos: number[] = carrito.map((item) => item.producto_id);

        const nuevoPedido: PedidoDTO= {
            pClienteId: Number(cliente.cliente_id),
            pEstado: "Pendiente",
            /*
            cliente_nombre: clienteNombre,
            fecha_pedido: new Date(),
            estado_pedido: "Pendiente",*/
            pCantidades: cantidad,
            pProductos: productos,
            /*
            total: carrito.reduce((acc, item) => acc + item.subtotal, 0),
            detalles: carrito.map((item) => ({
                producto_id: item.producto_id,
                cantidad: item.cantidad,
                precio_unitario: item.precio_unitario,
            })),*/
        };

        console.log(nuevoPedido.pClienteId); //here is not

        fetch("http://localhost:8080/api/v1/pedidos", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(nuevoPedido),
        })
            .then((res) => {
                if (!res.ok) {
                    throw new Error("Error al realizar el pedido.");
                }

            })
            .then(() => {
                fetchPedidos();
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
                setPedidos(pedidos.filter((pedido) => pedido.pedidoId !== pedido_id));
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
                        <div key={producto.productoId} className="border p-4 rounded shadow hover:shadow-lg bg-gray-50">
                            <h3 className="text-lg font-semibold mb-2">{producto.nombre}</h3>
                            <p className="text-sm text-gray-600 mb-2">{producto.descripcion}</p>
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
                            <li key={pedido.pedidoId} className="flex justify-between items-center mb-2 border-b pb-2">
                                Pedido #{pedido.pedidoId} - Cliente: {pedido.clienteNombre} - Total: ${pedido.total}
                                <button
                                    onClick={() => cancelarPedido(pedido.pedidoId)}
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