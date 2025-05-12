package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.valerio.tiendaapi.model.Clientes;

import java.util.Optional;

public interface ClientesRepository extends JpaRepository<Clientes, Integer> {

    @Query(nativeQuery = true, value="SELECT get_cliente_nombre_completo(:clienteId)")
    public String getClienteNombreCompleto(@Param("clienteId") Integer clienteId);

}
