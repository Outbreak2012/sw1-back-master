package com.nuevo.proyectonuevo.service;

import java.util.List;
import java.util.Optional;
import com.nuevo.proyectonuevo.entity.*;
import com.nuevo.proyectonuevo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagoService {

    @Autowired
    private PagoRepository repository;

    

    public List<Pago> findAll() {
        return repository.findAll();
    }

    public Optional<Pago> findById(int id) {
        return repository.findById(id);
    }

    public Pago save(Pago entity) {
        Pago nuevoPago = new Pago();
        nuevoPago.setEstado(entity.getEstado());
        nuevoPago.setMetodo(entity.getMetodo());
        nuevoPago.setMonto(entity.getMonto());
        nuevoPago.setFechapago(entity.getFechapago());
        return repository.save(nuevoPago);
    }

    public Pago edit(int id, Pago entity) {
        Pago existentePago = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado: " + id));
        existentePago.setEstado(entity.getEstado());
        existentePago.setMetodo(entity.getMetodo());
        existentePago.setMonto(entity.getMonto());
        existentePago.setFechapago(entity.getFechapago());
        return repository.save(existentePago);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

}
