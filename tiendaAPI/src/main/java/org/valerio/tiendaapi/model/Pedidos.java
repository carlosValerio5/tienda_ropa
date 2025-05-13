package org.valerio.tiendaapi.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Guille
 */
@Entity
@Table(name="pedidos")
public class Pedidos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pedidoId;

    @Column(columnDefinition = "DATE")
    private LocalDate fecha_pedido;
    private String estado_pedido;
    private Double total;

    @Transient
    private String clienteNombre;

    @ManyToOne
    @JoinColumn(name="cliente_id")
    private Clientes cliente;


    @Transient
    //@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallesPedido> detalles = new ArrayList<>();


    @PostLoad
    public void postLoad() {
        if (cliente != null) {
            this.clienteNombre = cliente.getNombre();
        }
    }

    public Pedidos() {
    }

    public Pedidos(Integer pedidoId){
        this.pedidoId = pedidoId;
    }



    public Pedidos(Integer pedidoId, LocalDate fecha_pedido, String estado_pedido, Double total, Clientes cliente) {
        this.pedidoId = pedidoId;
        this.fecha_pedido = fecha_pedido;
        this.estado_pedido = estado_pedido;
        this.total = total;
        this.cliente = cliente;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public List<DetallesPedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallesPedido> detalles) {
        this.detalles = detalles;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedido_id) {
        this.pedidoId = pedido_id;
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
