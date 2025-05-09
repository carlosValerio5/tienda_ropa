package org.valerio.tiendaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valerio.tiendaapi.dto.ProductosDTO;
import org.valerio.tiendaapi.exceptions.ProductoNoEncontradoException;
import org.valerio.tiendaapi.exceptions.ProductoYaExisteException;
import org.valerio.tiendaapi.mapper.ProductosMapper;
import org.valerio.tiendaapi.model.Categorias;
import org.valerio.tiendaapi.model.Marcas;
import org.valerio.tiendaapi.model.Productos;
import org.valerio.tiendaapi.service.CategoriasService;
import org.valerio.tiendaapi.service.MarcasService;
import org.valerio.tiendaapi.service.ProductosService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/productos")
public class ProductosController {

    private final ProductosService productosService;
    private final MarcasService marcasService;
    private final CategoriasService categoriasService;

    @Autowired
    public ProductosController(ProductosService productosService, MarcasService marcasService,
                               CategoriasService categoriasService) {
        this.productosService = productosService;
        this.marcasService = marcasService;
        this.categoriasService = categoriasService;
    }

    @GetMapping
    public List<Productos> getProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer id
    ) {
        if(id != null){
            //Este metodo retorna una sola instancia de Productos por lo que se debe convertir a singleton List
            return Collections.singletonList(productosService.getById(id));
        }else if (nombre != null){
            return productosService.getByName(nombre);
        }else {
            return productosService.getProductos();
        }
    }

    /**
     *
     * @param productoRequest
     * @return
     *
     * ProductosDTO sirve para que el usuario o el desarrollador frontEnd pueda consumir la api
     * de manera mas sencilla, el DTO permite ingresar solo el id de la marca o de la categoria
     * y el metodo se encarga de la logica de revisar que exista dicho objeto en la base de datos
     *
     * De lo contrario, el usuario tendria que especificar una instancia de cada objeto que
     * es miembro de esta entidad para poder añadirla y relacionarla con las demas tablas
     */
    @PostMapping
    public ResponseEntity<Productos> addProducto(@RequestBody ProductosDTO productoRequest) {
        Marcas marcas = null;
        Categorias categorias = null;
        if (productoRequest.marcaId() != null) {
            marcas = marcasService.getMarcaById(productoRequest.marcaId());
            if (marcas == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        if (productoRequest.categoriaId() != null) {
            categorias = categoriasService.getCategoriaPorID(productoRequest.categoriaId()).getFirst();
            if (categorias == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        //Creamos un mapper para facilitar la creacion del producto
        ProductosMapper productosMapper = new ProductosMapper();

        Productos productoCreado = productosMapper.apply(productoRequest);

        //Esto se actualiza fuera del mapper para no tener que inyectar dependencias en el mapper
        //Si se hiciera en el mapper tendriamos que inyectar los servicios de marca y categoria
        productoCreado.setMarca(marcas);
        productoCreado.setCategorias(categorias);

        Productos resultado;
        try{
            resultado = productosService.addProducto(productoCreado);
        } catch (ProductoYaExisteException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Productos> updateProducto(@RequestBody Productos producto) {

        if(producto.getProductoId() == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Productos resultado;
        try {
            resultado = productosService.updateProducto(producto);
        }catch (ProductoYaExisteException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(resultado, HttpStatus.OK);

    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<Productos> deleteProducto(@PathVariable Integer productoId) {

        Productos deletedProducto;

        try{
            deletedProducto = productosService.deleteProducto(productoId);
        }catch (ProductoNoEncontradoException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(deletedProducto, HttpStatus.OK);
    }

}
