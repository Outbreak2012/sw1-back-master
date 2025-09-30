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
@RequestMapping("/api/inventarios")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/get")
    public ResponseEntity<List<Inventario>> getAll() {
        List<Inventario> lista = inventarioService.findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Inventario> getById(@PathVariable int id) {
        Optional<Inventario> result = inventarioService.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/create")
    public ResponseEntity<Inventario> create(@RequestBody Inventario entity) {
        Inventario nuevo = inventarioService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Inventario> edit(@PathVariable int id, @RequestBody Inventario entity) {
        try {
            Inventario actualizado = inventarioService.edit(id, entity);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        try {
            inventarioService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
