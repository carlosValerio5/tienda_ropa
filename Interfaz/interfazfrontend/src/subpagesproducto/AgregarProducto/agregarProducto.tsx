import {useEffect, useState} from 'react';

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

const AgregarProducto = () => {
    const [productoId, setProductoId] = useState(0);
    const [productos, setProductos] = useState<Producto[]>([]);
    const [nombre, setNombre] = useState('');
    const [precio, setPrecio] = useState(0);
    const [stock, setStock] = useState(0);
    const [talla, setTalla] = useState('');
    const [descripcionCompleta, setDescripcionCompleta] = useState('');
    const [genero, setGenero] = useState('');
    const [categoria, setCategoria] = useState('');
    const [color, setColor] = useState('');
    const [marca, setMarca] = useState('');
    const [editando, setEditando] = useState<number | null>(null);
    const [cargando, setCargando] = useState(false);

    const renderLoader = () => cargando && <p>Cargando...</p>;
    const renderError = () => error && <p className="text-red-500">{error}</p>;
    const [error, setError] = useState<string | null>(null);


    const fetchProductos = async () => {
        setCargando(true);
        setError(null);
        try {
            const response = await fetch('http://localhost:8080/api/v1/productos');
            if (!response.ok) {
                throw new Error('Error al obtener los productos.');
            }
            const data: Producto[] = await response.json();


            setProductos(data);
        } catch {
            setError('No se pudieron cargar los productos.');
        } finally {
            setCargando(false);
        }
    };

    const agregarProducto = async () => {
        setCargando(true);
        setError(null);
        const nuevoProducto: Omit<Producto, 'productoId'> = {
            nombre,
            precio,
            stock,
            tallaId: talla,
            descripcion: descripcionCompleta,
            genero,
            categoriaNombre: categoria,
            color_id: color,
            marcaNombre: marca,
        };

        try {
            const response = await fetch('http://localhost:8080/api/v1/productos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(nuevoProducto),
            });
            if (!response.ok) {
                throw new Error('Error al agregar el producto.');
            }
            const productoCreado: Producto = await response.json();
            setProductos([...productos, productoCreado]);
            limpiarFormulario();
        } catch {
            setError('No se pudo agregar el producto.');
        } finally {
            setCargando(false);
        }
    };

    const eliminarProducto = async (id: number) => {
        setCargando(true);
        setError(null);

        try {
            const response = await fetch(`http://localhost:8080/api/v1/productos/${id}`, { method: 'DELETE' });
            if (!response.ok) {
                throw new Error('Error al eliminar el producto.');
            }
            setProductos(productos.filter(p => p.productoId !== id));
        } catch {
            setError('No se pudo eliminar el producto.');
        } finally {
            setCargando(false);
        }
    };

    const guardarEdicion = async () => {
        if (editando === null) return;
        setCargando(true);
        setError(null);

        const productoActualizado: Omit<Producto, ''> = {
            productoId,
            nombre,
            precio,
            stock,
            tallaId: talla,
            descripcion: descripcionCompleta,
            genero,
            categoriaNombre: categoria,
            color_id: color,
            marcaNombre: marca,
        };

        console.log("Producto actualizado:"+productoActualizado);

        try {
            const response = await fetch(`http://localhost:8080/api/v1/productos`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(productoActualizado),
            });
            if (!response.ok) {
                throw new Error('Error al guardar los cambios.');
            }
            const productoEditado: Producto = await response.json();
            setProductos(productos.map(p => (p.productoId === editando ? productoEditado : p)));
            setEditando(null);
            limpiarFormulario();
            fetchProductos();
        } catch {
            setError('No se pudieron guardar los cambios.');
        } finally {
            setCargando(false);
        }
    };

    const editarProducto = (producto: Producto) => {
        console.log(producto);
        setProductoId(producto.productoId??0)
        setNombre(producto.nombre ?? '');
        setPrecio(producto.precio ?? 0);
        setStock(producto.stock ?? 0);
        setTalla(producto.tallaId ?? '');
        setDescripcionCompleta(producto.descripcion ?? '');
        setGenero(producto.genero ?? '');
        setCategoria(producto.categoriaNombre ?? '');
        setColor(producto.color_id ?? '');
        setMarca(producto.marcaNombre ?? '');
        setEditando(producto.productoId);
    };

    const limpiarFormulario = () => {
        setNombre('');
        setPrecio(0);
        setStock(0);
        setTalla('');
        setDescripcionCompleta('');
        setGenero('');
        setCategoria('');
        setColor('');
        setMarca('');
    };

    useEffect(() => {
        fetchProductos();
    }, []);

    return (
        <div>
            {renderLoader()}
            {renderError()}
            <h1 className="text-4xl font-bold text-center mb-8 text-blue-600">
                Gestionar Productos
            </h1>
            <div className="bg-white shadow p-4 rounded mb-6">
                <h2 className="text-lg font-semibold mb-2">{editando ? 'Editar' : 'Agregar'} Producto</h2>
                <div className="grid grid-cols-4 md:grid-cols-8 gap-2">
                    <input type="text" placeholder="Nombre" value={nombre} onChange={e => setNombre(e.target.value)} className="border p-2 rounded" />
                    <input type="number" placeholder="Precio" value={precio} onChange={e => setPrecio(Number(e.target.value))} className="border p-2 rounded" />
                    <input type="number" placeholder="Stock" value={stock} onChange={e => setStock(Number(e.target.value))} className="border p-2 rounded" />
                    <select value={talla} onChange={e => setTalla(e.target.value)} className="border p-1 rounded">
                        <option value="">Selecciona una talla</option>
                        <option value="XS">XS</option>
                        <option value="S">S</option>
                        <option value="M">M</option>
                        <option value="L">L</option>
                        <option value="XL">XL</option>
                        <option value="XXL">XXL</option>
                    </select>
                    <input type="text" placeholder="Color" value={color} onChange={e => setColor(e.target.value)} className="border p-2 rounded" />
                    <input type="text" placeholder="Marca" value={marca} onChange={e => setMarca(e.target.value)} className="border p-2 rounded" />
                    <input type="text" placeholder="Categoria" value={categoria} onChange={e => setCategoria(e.target.value)} className="border p-2 rounded" />
                    <select value={genero} onChange={e => setGenero(e.target.value)} className="border p-2 rounded">
                        <option value="">Selecciona genero</option>
                        <option value="Hombre">Hombre</option>
                        <option value="Mujer">Mujer</option>
                    </select>
                    <textarea placeholder="Descripción" value={descripcionCompleta} onChange={e => setDescripcionCompleta(e.target.value)} className="border p-2 rounded resize-y min-h-[80px] col-span-6 md:col-span-9" />
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
                <h2 className="text-lg font-semibold p-2 border-b">Lista de Productos</h2>
                <table className="w-full table-auto">
                    <thead>
                    <tr className="bg-gray-100">
                        <th className="text-left p-2">Nombre</th>
                        <th className="text-left p-2">Precio</th>
                        <th className="text-left p-2">Stock</th>
                        <th className="text-left p-2">Talla</th>
                        <th className="text-left p-2">Descripcion</th>
                        <th className="text-left p-2">Genero</th>
                        <th className="text-left p-2">Categoria</th>
                        <th className="text-left p-2">Color</th>
                        <th className="text-left p-2">Marca</th>
                        <th className="text-left p-2">Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    {productos.map((p) => (
                        <tr key={p.productoId} className="border-t">
                            <td className="p-2">{p.nombre}</td>
                            <td className="p-2">${p.precio}</td>
                            <td className="p-2">{p.stock}</td>
                            <td className="p-2">{p.tallaId}</td>
                            <td className="p-2 max-w-[200px]">
                                <div className="max-h-[80px] overflow-y-auto break-words whitespace-pre-line">{p.descripcion}</div>
                            </td>
                            <td className="p-2">{p.genero}</td>
                            <td className="p-2">{p.categoriaNombre}</td>
                            <td className="p-2">{p.color_id}</td>
                            <td className="p-2">{p.marcaNombre}</td>
                            <td className="p-2 space-x-4">
                                <button onClick={() => editarProducto(p)} className="bg-blue-500 text-white px-3 py-1 rounded">Editar</button>
                                <button onClick={() => eliminarProducto(p.productoId)} className="bg-red-500 text-white px-3 py-1 rounded">Eliminar</button>
                            </td>
                        </tr>
                    ))}
                    {productos.length === 0 && (
                        <tr>
                            <td className="p-2" colSpan={10}>No hay productos registrados.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default AgregarProducto;