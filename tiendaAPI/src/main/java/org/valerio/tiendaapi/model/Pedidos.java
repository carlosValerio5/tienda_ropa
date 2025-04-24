package org.valerio.tiendaapi.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="pedidos")
public class Pedidos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer pedido_id;

    @Column(columnDefinition = "DATE")
    private LocalDate fecha_pedido;
    private String estado_pedido;
    private Double total;

    @ManyToOne
    @JoinColumn(name="cliente_id")
    private Clientes cliente;

    public Pedidos() {
    }

    public Pedidos(Integer pedido_id, LocalDate fecha_pedido, String estado_pedido, Double total, Clientes cliente) {
        this.pedido_id = pedido_id;
        this.fecha_pedido = fecha_pedido;
        this.estado_pedido = estado_pedido;
        this.total = total;
        this.cliente = cliente;
    }

    public Integer getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(Integer pedido_id) {
        this.pedido_id = pedido_id;
    }

    public LocalDate getFecha_pedido() {
        return fecha_pedido;
    }

    public void setFecha_pedido(LocalDate fecha_pedido) {
        this.fecha_pedido = fecha_pedido;
    }

    public String getEstado_pedido() {
        return estado_pedido;
    }

    public void setEstado_pedido(String estado_pedido) {
        this.estado_pedido = estado_pedido;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }
}
