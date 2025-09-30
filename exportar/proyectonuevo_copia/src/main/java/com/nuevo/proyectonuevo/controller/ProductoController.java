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
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/get")
    public ResponseEntity<List<Producto>> getAll() {
        List<Producto> lista = productoService.findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Producto> getById(@PathVariable int id) {
        Optional<Producto> result = productoService.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/create")
    public ResponseEntity<Producto> create(@RequestBody Producto entity) {
        Producto nuevo = productoService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Producto> edit(@PathVariable int id, @RequestBody Producto entity) {
        try {
            Producto actualizado = productoService.edit(id, entity);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        try {
            productoService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
