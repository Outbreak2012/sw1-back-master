package com.nuevo.proyectonuevo.service;

import java.util.List;
import java.util.Optional;
import com.nuevo.proyectonuevo.entity.*;
import com.nuevo.proyectonuevo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    

    public List<Producto> findAll() {
        return repository.findAll();
    }

    public Optional<Producto> findById(int id) {
        return repository.findById(id);
    }

    public Producto save(Producto entity) {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setImagenurl(entity.getImagenurl());
        nuevoProducto.setDescripcion(entity.getDescripcion());
        nuevoProducto.setNombre(entity.getNombre());
        nuevoProducto.setPeso(entity.getPeso());
        nuevoProducto.setPrecio(entity.getPrecio());
        return repository.save(nuevoProducto);
    }

    public Producto edit(int id, Producto entity) {
        Producto existenteProducto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
        existenteProducto.setImagenurl(entity.getImagenurl());
        existenteProducto.setDescripcion(entity.getDescripcion());
        existenteProducto.setNombre(entity.getNombre());
        existenteProducto.setPeso(entity.getPeso());
        existenteProducto.setPrecio(entity.getPrecio());
        return repository.save(existenteProducto);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

}
