package org.valerio.tiendaapi.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer inventario_id;
    private Long cantidad;

    @Column(columnDefinition = "DATE")
    private LocalDate fecha_actualizacion;

    @ManyToOne
    @JoinColumn(name="producto_id")
    private Productos producto;

    public Inventario() {
    }

    public Inventario(Integer inventario_id, Long cantidad, LocalDate fecha_actualizacion, Productos producto) {
        this.inventario_id = inventario_id;
        this.cantidad = cantidad;
        this.fecha_actualizacion = fecha_actualizacion;
        this.producto = producto;
    }

    public Integer getInventario_id() {
        return inventario_id;
    }

    public void setInventario_id(Integer inventario_id) {
        this.inventario_id = inventario_id;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(LocalDate fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }
}
