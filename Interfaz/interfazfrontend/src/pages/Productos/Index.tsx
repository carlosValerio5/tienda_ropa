import { useState, useEffect } from 'react';

const API_URL = "http://localhost:8080/";

interface Producto {
    productoId: number;
    nombre: string;
    precio: number;
    stock: number;
}

const Productos = () => {
    const [productos, setProductos] = useState<Producto[]>([]);
    const [nombre, setNombre] = useState('');
    const [precio, setPrecio] = useState(0);
    const [stock, setStock] = useState(0);
    const [editando, setEditando] = useState<number | null>(null);

    useEffect (() => {
        const fetchData = async () => {
            try{
                const response = await fetch("http://localhost:8080/api/v1/productos");
                const json = await response.json();
                setProductos(json);
                console.log(json);
            }catch (error){
                console.log(error);
            }
        }
        fetchData();
    }, []);


    const agregarProducto = async () => {
        const nuevo: Producto = {
            productoId: Date.now(),
            nombre,
            precio,
            stock,
        };
        const rawResponse = await fetch(API_URL + "/productos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(nuevo),
        });
        const content = await rawResponse.json();
        console.log(content);
        setProductos([...productos, nuevo]);
        limpiarFormulario();
    };

    const eliminarProducto = (id: number) => {
        setProductos(productos.filter(p => p.productoId !== id));
    };

    const editarProducto = (producto: Producto) => {
        setNombre(producto.nombre);
        setPrecio(producto.precio);
        setStock(producto.stock);
        setEditando(producto.productoId);
    };

    const guardarEdicion = () => {
        setProductos(productos.map(p =>
            p.productoId === editando ? { ...p, nombre, precio, stock } : p
        ));
        setEditando(null);
        limpiarFormulario();
    };

    const limpiarFormulario = () => {
        setNombre('');
        setPrecio(0);
        setStock(0);
    };

    return (
        <div>
            <h1 className="text-2xl font-bold mb-4">Gestión de Productos</h1>

            <div className="bg-white shadow p-4 rounded mb-6">
                <h2 className="text-lg font-semibold mb-2">{editando ? 'Editar' : 'Agregar'} Producto</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <input
                        type="text"
                        placeholder="Nombre"
                        value={nombre}
                        onChange={e => setNombre(e.target.value)}
                        className="border p-2 rounded"
                    />
                    <input
                        type="number"
                        placeholder="Precio"
                        value={precio}
                        onChange={e => setPrecio(Number(e.target.value))}
                        className="border p-2 rounded"
                    />
                    <input
                        type="number"
                        placeholder="Stock"
                        value={stock}
                        onChange={e => setStock(Number(e.target.value))}
                        className="border p-2 rounded"
                    />
                </div>
                <div className="mt-4">
                    {editando ? (
                        <button onClick={guardarEdicion} className="bg-yellow-500 text-white px-4 py-2 rounded mr-2">Guardar</button>
                    ) : (
                        <button onClick={agregarProducto} className="bg-green-600 text-white px-4 py-2 rounded mr-2">Agregar</button>
                    )}
                    <button onClick={limpiarFormulario} className="bg-gray-400 text-white px-4 py-2 rounded">Cancelar</button>
                </div>
            </div>

            <div className="bg-white shadow rounded">
                <h2 className="text-lg font-semibold p-4 border-b">Lista de Productos</h2>
                <table className="w-full table-auto">
                    <thead>
                    <tr className="bg-gray-100">
                        <th className="text-left p-2">Nombre</th>
                        <th className="text-left p-2">Precio</th>
                        <th className="text-left p-2">Stock</th>
                        <th className="text-left p-2">Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    {productos.map((p) => (
                        <tr key={p.productoId} className="border-t">
                            <td className="p-2">{p.nombre}</td>
                            <td className="p-2">${p.precio}</td>
                            <td className="p-2">{p.stock}</td>
                            <td className="p-2 space-x-2">
                                <button onClick={() => editarProducto(p)} className="bg-blue-500 text-white px-3 py-1 rounded">Editar</button>
                                <button onClick={() => eliminarProducto(p.productoId)} className="bg-red-500 text-white px-3 py-1 rounded">Eliminar</button>
                            </td>
                        </tr>
                    ))}
                    {productos.length === 0 && (
                        <tr>
                            <td className="p-2" colSpan={4}>No hay productos registrados.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Productos;
