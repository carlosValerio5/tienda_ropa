package org.valerio.tiendaapi.model;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Entity
@Table(name="inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventarioId;
    private Long cantidad;

    @Column(columnDefinition = "DATE")
    private LocalDate fecha_actualizacion;

    @ManyToOne
    @JoinColumn(name="producto_id", nullable=false)
    private Productos producto;

    public Inventario() {
    }

    public Inventario(Long cantidad, Productos producto) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.fecha_actualizacion = LocalDate.now();
    }

    public Inventario(Integer inventarioId, Long cantidad, LocalDate fecha_actualizacion, Productos producto) {
        this.inventarioId = inventarioId;
        this.cantidad = cantidad;
        this.fecha_actualizacion = fecha_actualizacion;
        this.producto = producto;
    }

    public Inventario(Integer inventarioId, Long cantidad, Productos producto) {
        this.inventarioId = inventarioId;
        this.cantidad = cantidad;
        this.producto = producto;
        this.fecha_actualizacion = LocalDate.now();
    }

    public Integer getInventarioId() {
        return inventarioId;
    }

    public void setInventarioId(Integer inventario_id) {
        this.inventarioId = inventario_id;
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
