package org.valerio.tiendaapi.model;

import jakarta.persistence.*;

@Entity
@Table(name="productos")
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productoId;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String talla_id;
    private String color_id;
    private Long stock;
    private String genero;

    @Transient
    private String categoriaNombre;

    @Transient
    private String marcaNombre;

    @ManyToOne
    @JoinColumn(name="marca_id")
    private Marcas marca;

    @ManyToOne
    @JoinColumn(name="categoria_id")
    private Categorias categorias;

    public Productos() {
    }

    public Productos(Integer productoId, String nombre, String descripcion, Double precio, String talla_id, String color_id,
                     Long stock, String genero, Marcas marca, Categorias categorias, String categoriaNombre, String marcaNombre) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.talla_id = talla_id;
        this.color_id = color_id;
        this.stock = stock;
        this.genero = genero;
        this.marca = marca;
        this.categorias = categorias;
        this.categoriaNombre = categoriaNombre;
        this.marcaNombre = marcaNombre;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public String getMarcaNombre() {
        return marcaNombre;
    }

    public void setMarcaNombre(String marcaNombre) {
        this.marcaNombre = marcaNombre;
    }

    public Marcas getMarca() {
        return marca;
    }

    public void setMarca(Marcas marca) {
        this.marca = marca;
    }

    public Categorias getCategorias() {
        return categorias;
    }

    public void setCategorias(Categorias categorias) {
        this.categorias = categorias;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer producto_id) {
        this.productoId = producto_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getTalla_id() {
        return talla_id;
    }

    public void setTalla_id(String talla_id) {
        this.talla_id = talla_id;
    }

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
