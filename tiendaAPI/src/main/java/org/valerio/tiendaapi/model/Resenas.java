package org.valerio.tiendaapi.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="reseñas")
public class Resenas {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer resena_id;
    private Integer calificacion;
    private String comentario;

    @Column(columnDefinition = "DATE")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name="producto_id")
    private Productos producto;

    @ManyToOne
    @JoinColumn(name="cliente_id")
    private Clientes cliente;

    public Resenas() {
    }

    public Resenas(Integer resena_id, Integer calificacion, String comentario, LocalDate fecha, Productos producto, Clientes cliente) {
        this.resena_id = resena_id;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fecha = fecha;
        this.producto = producto;
        this.cliente = cliente;
    }

    public Integer getResena_id() {
        return resena_id;
    }

    public void setResena_id(Integer resena_id) {
        this.resena_id = resena_id;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }
}
