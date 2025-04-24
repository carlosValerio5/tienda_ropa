package org.valerio.tiendaapi.model;

import jakarta.persistence.*;

@Entity
@Table(name="detalles_pedido")
public class DetallesPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer detalle_id;
    private Integer cantidad;
    private Double precio_unitario;

    @ManyToOne
    @JoinColumn(name="pedido_id")
    private Pedidos pedido;

    @ManyToOne
    @JoinColumn(name="producto_id")
    private Productos producto;


    public DetallesPedido() {}

    public DetallesPedido(Integer detalle_id, Integer cantidad, Double precio_unitario, Pedidos pedido, Productos producto) {
        this.detalle_id = detalle_id;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.pedido = pedido;
        this.producto = producto;
    }

    public Integer getDetalle_id() {
        return detalle_id;
    }

    public void setDetalle_id(Integer detalle_id) {
        this.detalle_id = detalle_id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public Pedidos getPedido() {
        return pedido;
    }

    public void setPedido(Pedidos pedido) {
        this.pedido = pedido;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }
}
