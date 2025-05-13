package org.valerio.tiendaapi.service;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.exceptions.ClienteNoExisteExeption;
import org.valerio.tiendaapi.exceptions.PedidoNotFoundException;
import org.valerio.tiendaapi.model.DetallesPedido;
import org.valerio.tiendaapi.model.Pedidos;
import org.valerio.tiendaapi.model.Productos;
import org.valerio.tiendaapi.repository.ClientesRepository;
import org.valerio.tiendaapi.repository.DetallesPedidoRepository;
import org.valerio.tiendaapi.repository.PedidosRepository;
import org.valerio.tiendaapi.repository.ProductosRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidosService {
    @Autowired
    private PedidosRepository pedidosRepository;
    @Autowired
    private ProductosRepository productosRepository;
    @Autowired
    private ClientesRepository clientesRepository;
    @Autowired
    private DetallesPedidoRepository detallesPedidoRepository;
    @Autowired
    private DetallesPedidosService detallesPedidosService;

    public void crearPedido(Integer idCliente, String pEstado, Integer[] pProductos, Integer[] pCantidades) {
        try {
            pedidosRepository.crearPedido(idCliente, pEstado, pProductos, pCantidades);
        } catch (ConstraintViolationException e) {
            throw new RuntimeException("Error al crear pedido", e);
        }
    }


    public Pedidos crearPedido(Pedidos pedido)throws ClienteNoExisteExeption {
        if (!clientesRepository.existsById(pedido.getCliente().getCliente_id())) {
            throw new ClienteNoExisteExeption("Cliente no existe");
        }
        double total = 0;
        for (DetallesPedido detalle : pedido.getDetalles()) {
            Productos producto = productosRepository.findById(detalle.getProducto().getProductoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado con ID: " + detalle.getProducto().getProductoId()
                    ));
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException(
                        "Stock insuficiente para el producto: " + producto.getNombre() +
                                ". Stock actual: " + producto.getStock() +
                                ", solicitado: " + detalle.getCantidad()
                );
            }
            detalle.setPrecio_unitario(producto.getPrecio());
            total += detalle.getPrecio_unitario() * detalle.getCantidad();

            producto.setStock(producto.getStock() - detalle.getCantidad());
            productosRepository.save(producto);

            detalle.setPedido(pedido);

        }

        pedido.setTotal(total);
        pedido.setFecha_pedido(LocalDate.now());
        pedido.setEstado_pedido("PENDIENTE");

        return pedidosRepository.save(pedido);
    }



    public DetallesPedido agregarDetalleAPedido(Integer pedidoId, DetallesPedido detalle) {
        Pedidos pedido = pedidosRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Validar producto y stock (similar a crearPedido)
        Productos producto = productosRepository.findById(detalle.getProducto().getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < detalle.getCantidad()) {
            throw new RuntimeException("Stock insuficiente");
        }

        // Actualizar stock
        producto.setStock(producto.getStock() - detalle.getCantidad());
        productosRepository.save(producto);

        // Vincular con el pedido
        detalle.setPedido(pedido);
        return detallesPedidoRepository.save(detalle);
    }

    public List<DetallesPedido> obtenerDetallesPorPedido(Pedidos pedido) {
        return detallesPedidoRepository.findByPedido(pedido);
    }

    public List<Pedidos> getPedidos(){
        return pedidosRepository.findAll();
    }

    public void eliminarPedido(Integer idPedido) throws PedidoNotFoundException {

        Pedidos pedido = pedidosRepository.getPedidosByPedidoId(idPedido);

        if (pedido == null) {
            throw new PedidoNotFoundException("Pedido no encontrado");
        }

        pedidosRepository.delete(pedido);
    }

}
