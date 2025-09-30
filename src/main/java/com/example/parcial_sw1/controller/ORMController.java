package com.example.parcial_sw1.controller;

import com.example.parcial_sw1.service.ORMService;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;

@RestController
@RequestMapping("/user/exportar")
public class ORMController {
    private final ORMService ormService;

    public ORMController(ORMService ormService) {
        this.ormService = ormService;
    }

    @GetMapping("/{proyectoId}/generar-entidades")
    public ResponseEntity<FileSystemResource> generarEntidades(@PathVariable int proyectoId) {
        try {
            String rutaZip = ormService.generarEntidadesEnCopia(proyectoId);
            File file = new File(rutaZip);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new FileSystemResource(file));
        } catch (Exception e) {
             System.out.println("Error al generar entidades: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}