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
    producto_id INT,
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
    JOIN clientes c on ped.cliente_id = c.cliente_id
