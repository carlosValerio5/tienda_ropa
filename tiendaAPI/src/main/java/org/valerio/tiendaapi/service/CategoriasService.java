package org.valerio.tiendaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.exceptions.CategoriaNotFoundException;
import org.valerio.tiendaapi.model.Categorias;
import org.valerio.tiendaapi.repository.CategoriasRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoriasService {
    private final CategoriasRepository categoriasRepository;

    @Autowired
    public CategoriasService(CategoriasRepository categoriasRepository) {
        this.categoriasRepository = categoriasRepository;
    }

    public List<Categorias> getCategorias() { return categoriasRepository.findAll(); }

    public List<Categorias> getCategoriaPorID(Integer id) {
        return categoriasRepository.findAll().stream().filter(
                categorias -> Objects.equals(categorias.getCategoriaId(), id)
        ).toList();
    }

    public List<Categorias> getCategoriaPorNombre(String nombre) {
        return categoriasRepository.findAll().stream().filter(
                categorias -> Objects.equals(categorias.getNombre(), nombre)
        ).toList();
    }

    public Categorias updateCategorias(Categorias categorias) {
        Optional<Categorias> categoriaToUpdate = categoriasRepository.findByCategoriaId(categorias.getCategoriaId());

        if (categoriaToUpdate.isPresent()) {
            categoriaToUpdate.get().setNombre(categorias.getNombre());
            categoriasRepository.save(categoriaToUpdate.get());
            return categoriaToUpdate.get();
        }
        return null;
    }

    public Categorias deleteCategorias(Integer id) throws CategoriaNotFoundException {
        Optional<Categorias> categoriaToDelete = categoriasRepository.findByCategoriaId(id);
        if (categoriaToDelete.isPresent()) {
            categoriasRepository.delete(categoriaToDelete.get());
            return categoriaToDelete.get();
        }
        throw new CategoriaNotFoundException("Categoria no encontrada");
    }

    public Categorias addCategorias(Categorias categoria) {
        if(categoria.getNombre() == null) {
            return null;
        }
        categoriasRepository.save(categoria);
        return categoria;
    }
}
