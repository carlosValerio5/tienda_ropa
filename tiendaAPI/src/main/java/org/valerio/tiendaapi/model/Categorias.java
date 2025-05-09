package org.valerio.tiendaapi.model;

import jakarta.persistence.*;

@Entity
@Table(name="categorias")
public class Categorias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoriaId;

    @Column(unique = true)
    private String nombre;

    public Categorias() {
    }

    public Categorias(Integer categoriaId, String nombre) {
        this.categoriaId = categoriaId;
        this.nombre = nombre;
    }

    public Categorias(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoria_id) {
        this.categoriaId = categoria_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
