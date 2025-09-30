package com.nuevo.proyectonuevo.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nuevo.proyectonuevo.entity.*;
import com.nuevo.proyectonuevo.service.*;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/get")
    public ResponseEntity<List<Categoria>> getAll() {
        List<Categoria> lista = categoriaService.findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable int id) {
        Optional<Categoria> result = categoriaService.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/create")
    public ResponseEntity<Categoria> create(@RequestBody Categoria entity) {
        Categoria nuevo = categoriaService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Categoria> edit(@PathVariable int id, @RequestBody Categoria entity) {
        try {
            Categoria actualizado = categoriaService.edit(id, entity);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        try {
            categoriaService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
