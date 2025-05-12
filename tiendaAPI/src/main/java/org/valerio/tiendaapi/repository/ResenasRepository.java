package org.valerio.tiendaapi.repository;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Resenas;

import java.util.List;



public interface ResenasRepository extends JpaRepository<Resenas, Integer> {
    @ManyToOne
    @JoinColumn(name = "producto_Id")

    List<Resenas>findByProductoProductoId(Integer id);
}
