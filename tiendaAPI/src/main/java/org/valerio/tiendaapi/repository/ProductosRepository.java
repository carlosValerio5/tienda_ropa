package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.valerio.tiendaapi.model.Productos;

import java.util.List;
import java.util.Optional;

public interface ProductosRepository extends JpaRepository<Productos, Integer> {

    //Metodo para verificar/actualizar stock
    @Modifying
    @Query("UPDATE Productos p SET p.stock = p.stock - :cantidad WHERE p.productoId = :productoId AND p.stock >= :cantidad")
    int reducirStock(@Param("productoId")Integer productoId, @Param("cantidad")Integer cantidad);

    Optional<Productos> findByProductoId(Integer productoId);

    List<Productos> findByNombreContainingIgnoreCase(String nombre);
}
