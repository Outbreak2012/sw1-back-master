package com.nuevo.proyectonuevo.service;

import java.util.List;
import java.util.Optional;
import com.nuevo.proyectonuevo.entity.*;
import com.nuevo.proyectonuevo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DireccionService {

    @Autowired
    private DireccionRepository repository;

    

    public List<Direccion> findAll() {
        return repository.findAll();
    }

    public Optional<Direccion> findById(int id) {
        return repository.findById(id);
    }

    public Direccion save(Direccion entity) {
        Direccion nuevoDireccion = new Direccion();
        nuevoDireccion.setPais(entity.getPais());
        nuevoDireccion.setCodigopostal(entity.getCodigopostal());
        nuevoDireccion.setProvincia(entity.getProvincia());
        nuevoDireccion.setCiudad(entity.getCiudad());
        nuevoDireccion.setCalle(entity.getCalle());
        return repository.save(nuevoDireccion);
    }

    public Direccion edit(int id, Direccion entity) {
        Direccion existenteDireccion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Direccion no encontrado: " + id));
        existenteDireccion.setPais(entity.getPais());
        existenteDireccion.setCodigopostal(entity.getCodigopostal());
        existenteDireccion.setProvincia(entity.getProvincia());
        existenteDireccion.setCiudad(entity.getCiudad());
        existenteDireccion.setCalle(entity.getCalle());
        return repository.save(existenteDireccion);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

}
