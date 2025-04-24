package org.valerio.tiendaapi.model;

import jakarta.persistence.*;

@Entity
@Table(name="marcas")
public class Marcas {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer marca_id;

    @Column(unique=true)
    private String nombre;


    public Marcas() {
    }

    public Marcas(Integer marca_id, String nombre) {
        this.marca_id = marca_id;
        this.nombre = nombre;
    }

    public Integer getMarca_id() {
        return marca_id;
    }

    public void setMarca_id(Integer marca_id) {
        this.marca_id = marca_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
