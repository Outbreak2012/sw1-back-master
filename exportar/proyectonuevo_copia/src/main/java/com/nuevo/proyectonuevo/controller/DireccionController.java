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
@RequestMapping("/api/direccions")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @GetMapping("/get")
    public ResponseEntity<List<Direccion>> getAll() {
        List<Direccion> lista = direccionService.findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Direccion> getById(@PathVariable int id) {
        Optional<Direccion> result = direccionService.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/create")
    public ResponseEntity<Direccion> create(@RequestBody Direccion entity) {
        Direccion nuevo = direccionService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Direccion> edit(@PathVariable int id, @RequestBody Direccion entity) {
        try {
            Direccion actualizado = direccionService.edit(id, entity);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        try {
            direccionService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
