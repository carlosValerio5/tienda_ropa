CREATE TABLE clientes(
    cliente_id SERIAL,
    nombre TEXT,
    apellido TEXT,
    email TEXT,
    telefono TEXT,
    direccion TEXT,
    fecha_registro DATE,
    PRIMARY KEY (cliente_id)
);

CREATE TABLE productos(
    producto_id SERIAL,
    nombre TEXT,
    descripcion VARCHAR(100),
    precio DOUBLE PRECISION,
    categoria_id INT,
    marca_id INT,
    talla_id TEXT,
    color_id TEXT,
    stock BIGINT,
    genero TEXT,
    PRIMARY KEY (producto_id),
    FOREIGN KEY (categoria_id) REFERENCES categorias(categoria_id),
    FOREIGN KEY (marca_id) REFERENCES marcas(marca_id)
);

CREATE TABLE categorias(
    categoria_id SERIAL,
    nombre TEXT,
    PRIMARY KEY (categoria_id),
    CONSTRAINT nombre_unico UNIQUE (nombre)
);

CREATE TABLE marcas(
    marca_id SERIAL,
    nombre TEXT,
    PRIMARY KEY (marca_id),
    CONSTRAINT nombre_marca_unico UNIQUE (nombre)
);

CREATE TABLE pedidos(
    pedido_id SERIAL,
    cliente_id INT,
    fecha_pedido DATE,
    estado_pedido TEXT,
    total DOUBLE PRECISION,
    PRIMARY KEY (pedido_id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id)
);

CREATE TABLE detalles_pedido(
    detalle_id SERIAL,
    pedido_id INT,
    producto_id INT,
    cantidad INT,
    precio_unitario DOUBLE PRECISION,
    PRIMARY KEY (detalle_id),
    FOREIGN KEY (pedido_id) REFERENCES pedidos(pedido_id),
    FOREIGN KEY (producto_id) REFERENCES productos(producto_id)
);

CREATE TABLE inventario(
    inventario_id SERIAL,
    producto_id INT NOT NULL,
    cantidad BIGINT,
    fecha_actualizacion DATE,
    PRIMARY KEY (inventario_id),
    FOREIGN KEY (producto_id) REFERENCES productos(producto_id)
);

CREATE TABLE reseñas(
    reseña_id SERIAL,
    producto_id INT,
    cliente_id INT,
    calificacion INT,
    comentario TEXT,
    fecha DATE,
    PRIMARY KEY (reseña_id),
    FOREIGN KEY (producto_id) REFERENCES productos(producto_id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id)
);

CREATE VIEW productos_detallados
AS
    SELECT p.nombre, p.descripcion, p.precio, p.talla_id, p.color_id, c.nombre as nombre_categoria, m.nombre as nombre_marca
    FROM productos p
    JOIN categorias c on p.categoria_id = c.categoria_id
    JOIN marcas m ON m.marca_id = p.marca_id;

CREATE VIEW pedidos_clientes
AS
    SELECT c.nombre, c.apellido, c.direccion, c.telefono, p.estado_pedido, p.fecha_pedido FROM pedidos p
    INNER JOIN clientes c on p.cliente_id = c.cliente_id;

CREATE VIEW inventario_productos
AS
    SELECT p.nombre, p.descripcion, i.cantidad, i.fecha_actualizacion FROM inventario i
    INNER JOIN productos p on i.producto_id = p.producto_id;

CREATE VIEW reseñas_productos
AS
    SELECT p.nombre, c.nombre as nombre_cliente, r.calificacion, r.comentario, r.fecha FROM productos p
    JOIN reseñas r on p.producto_id = r.producto_id
    JOIN clientes c on r.cliente_id = c.cliente_id;

CREATE VIEW detalles_pedido_completos
AS
    SELECT prod.nombre, dp.cantidad, c.nombre as nombre_cliente, prod.precio, ped.total FROM detalles_pedido dp
    JOIN productos prod on dp.producto_id = prod.producto_id
    JOIN pedidos ped ON dp.pedido_id = ped.pedido_id
    JOIN clientes c on ped.cliente_id = c.cliente_id;


-- Esta funcion simplemente concatena el nombre y apellido para un cliente en especifico.
CREATE FUNCTION get_cliente_nombre_completo(cliente_id INT)
    RETURNS TEXT AS $$
SELECT nombre || ' ' || apellido FROM clientes WHERE clientes.cliente_id = $1;
$$ LANGUAGE SQL;

/* Funcion para obtener el stock de un producto en especifico
   Cabe recalcar que por el momento un producto puede tener distintos inventarios
   por el momento (sujeto a cambios) por lo tanto esta funcion suma todas las
   cantidades de los diferentes inventarios para cada producto.
   */
CREATE FUNCTION get_stock_producto(prod_id INT)
RETURNS BIGINT AS $$
    SELECT COALESCE(SUM(cantidad),0) FROM inventario WHERE producto_id = $1
$$ LANGUAGE SQL;


-- Funcion para actualizar el inventario de un producto en especifico
CREATE OR REPLACE PROCEDURE actualizar_inventario(
    IN p_producto_id INT,
    IN p_cantidad BIGINT
)
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO inventario(producto_id, cantidad, fecha_actualizacion)
    VALUES (p_producto_id, p_cantidad, CURRENT_DATE);
END;
$$;



/*
Stored procedure, sirve para crear nuevas ordenes
Params:
    id del cliente
    estado del pedido
    productos a agregar a la orden -> array
    cantidades de cada producto -> array

Este procedure inserta primero los datos hacia una fila de la tabla pedidos
despues inserta tambien a la tabla detalles pedidos y por ultimo calcula el total de la orden
para poder actualizarlo en la tabla de pedidos

*/
CREATE OR REPLACE PROCEDURE crear_pedido(
    IN p_cliente_id INT,
    IN p_estado TEXT,
    IN p_productos INT[],
    IN p_cantidades INT[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    new_pedido_id INT;
    total_pedido DOUBLE PRECISION := 0;
    i INT := 1;
    precio_unit DOUBLE PRECISION;
    new_stock BIGINT;
BEGIN
    INSERT INTO pedidos(cliente_id, fecha_pedido, estado_pedido, total)
    VALUES (p_cliente_id, CURRENT_DATE, p_estado, 0)
    RETURNING pedido_id INTO new_pedido_id;

    WHILE i <= array_length(p_productos, 1) LOOP
        SELECT precio INTO precio_unit FROM productos WHERE producto_id = p_productos[i];

        SELECT stock FROM productos WHERE producto_id = p_productos[i] INTO new_stock;

        IF new_stock<p_cantidades[i] THEN
            i:=i+1;
            CONTINUE;
        end if;

        INSERT INTO detalles_pedido(pedido_id, producto_id, cantidad, precio_unitario)
        VALUES (new_pedido_id, p_productos[i], p_cantidades[i], precio_unit);


        new_stock := new_stock - p_cantidades[i];

        UPDATE productos
            SET stock = new_stock
        WHERE producto_id = p_productos[i];

        total_pedido := total_pedido + (precio_unit * p_cantidades[i]);
        i := i + 1;
    END LOOP;

    UPDATE pedidos SET total = total_pedido WHERE pedido_id = new_pedido_id;
END;
$$


alter table public.detalles_pedido
    drop constraint detalles_pedido_pedido_id_fkey;

alter table public.detalles_pedido
    add foreign key (pedido_id) references public.pedidos
        on delete cascade;