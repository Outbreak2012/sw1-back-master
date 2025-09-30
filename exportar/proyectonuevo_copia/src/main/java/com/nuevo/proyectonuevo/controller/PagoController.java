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
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping("/get")
    public ResponseEntity<List<Pago>> getAll() {
        List<Pago> lista = pagoService.findAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Pago> getById(@PathVariable int id) {
        Optional<Pago> result = pagoService.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/create")
    public ResponseEntity<Pago> create(@RequestBody Pago entity) {
        Pago nuevo = pagoService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Pago> edit(@PathVariable int id, @RequestBody Pago entity) {
        try {
            Pago actualizado = pagoService.edit(id, entity);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        try {
            pagoService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
