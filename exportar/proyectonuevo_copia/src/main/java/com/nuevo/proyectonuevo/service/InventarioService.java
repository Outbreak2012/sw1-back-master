package com.nuevo.proyectonuevo.service;

import java.util.List;
import java.util.Optional;
import com.nuevo.proyectonuevo.entity.*;
import com.nuevo.proyectonuevo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository repository;

    

    public List<Inventario> findAll() {
        return repository.findAll();
    }

    public Optional<Inventario> findById(int id) {
        return repository.findById(id);
    }

    public Inventario save(Inventario entity) {
        Inventario nuevoInventario = new Inventario();
        nuevoInventario.setUbicacion(entity.getUbicacion());
        nuevoInventario.setCantidaddisponible(entity.getCantidaddisponible());
        return repository.save(nuevoInventario);
    }

    public Inventario edit(int id, Inventario entity) {
        Inventario existenteInventario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado: " + id));
        existenteInventario.setUbicacion(entity.getUbicacion());
        existenteInventario.setCantidaddisponible(entity.getCantidaddisponible());
        return repository.save(existenteInventario);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

}
