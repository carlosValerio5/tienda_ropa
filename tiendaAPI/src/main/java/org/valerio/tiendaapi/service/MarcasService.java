package org.valerio.tiendaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.valerio.tiendaapi.model.Marcas;
import org.valerio.tiendaapi.repository.MarcasRepository;

import java.util.List;

@Service
public class MarcasService {
    @Autowired
    private MarcasRepository marcasRepository;

    public List<Marcas> getAllMarcas() {
        return marcasRepository.findAll();
    }

    public Marcas getMarcaById(Integer id) {
        return marcasRepository.findById(id);
    }

    public Marcas createMarca(Marcas marca) {
        return marcasRepository.save(marca);
    }

    public Marcas updateMarca(Integer id, Marcas marca) {
        Marcas marcaExistente = getMarcaById(id);
        marcaExistente.setNombre(marca.getNombre());
        return marcasRepository.save(marcaExistente);
    }

    public void deleteMarca(Integer id) {
        marcasRepository.deleteById(id);
    }
}