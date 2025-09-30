package com.nuevo.proyectonuevo.service;

import java.util.List;
import java.util.Optional;
import com.nuevo.proyectonuevo.entity.*;
import com.nuevo.proyectonuevo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    

    public List<Categoria> findAll() {
        return repository.findAll();
    }

    public Optional<Categoria> findById(int id) {
        return repository.findById(id);
    }

    public Categoria save(Categoria entity) {
        Categoria nuevoCategoria = new Categoria();
        nuevoCategoria.setDescripcion(entity.getDescripcion());
        nuevoCategoria.setNombre(entity.getNombre());
        return repository.save(nuevoCategoria);
    }

    public Categoria edit(int id, Categoria entity) {
        Categoria existenteCategoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrado: " + id));
        existenteCategoria.setDescripcion(entity.getDescripcion());
        existenteCategoria.setNombre(entity.getNombre());
        return repository.save(existenteCategoria);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

}
