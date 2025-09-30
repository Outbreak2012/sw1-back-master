package com.nuevo.proyectonuevo.service;

import java.util.List;
import java.util.Optional;
import com.nuevo.proyectonuevo.entity.*;
import com.nuevo.proyectonuevo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    

    public List<Usuario> findAll() {
        return repository.findAll();
    }

    public Optional<Usuario> findById(int id) {
        return repository.findById(id);
    }

    public Usuario save(Usuario entity) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setContrasena(entity.getContrasena());
        nuevoUsuario.setEmail(entity.getEmail());
        nuevoUsuario.setNombre(entity.getNombre());
        nuevoUsuario.setFecharegistro(entity.getFecharegistro());
        return repository.save(nuevoUsuario);
    }

    public Usuario edit(int id, Usuario entity) {
        Usuario existenteUsuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        existenteUsuario.setContrasena(entity.getContrasena());
        existenteUsuario.setEmail(entity.getEmail());
        existenteUsuario.setNombre(entity.getNombre());
        existenteUsuario.setFecharegistro(entity.getFecharegistro());
        return repository.save(existenteUsuario);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

}
