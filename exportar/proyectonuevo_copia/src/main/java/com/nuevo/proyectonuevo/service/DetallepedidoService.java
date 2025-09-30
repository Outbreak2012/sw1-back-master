package com.nuevo.proyectonuevo.service;

import java.util.List;
import java.util.Optional;
import com.nuevo.proyectonuevo.entity.*;
import com.nuevo.proyectonuevo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetallepedidoService {

    @Autowired
    private DetallepedidoRepository repository;

    

    public List<Detallepedido> findAll() {
        return repository.findAll();
    }

    public Optional<Detallepedido> findById(int id) {
        return repository.findById(id);
    }

    public Detallepedido save(Detallepedido entity) {
        Detallepedido nuevoDetallepedido = new Detallepedido();
        nuevoDetallepedido.setCantidad(entity.getCantidad());
        nuevoDetallepedido.setPreciounitario(entity.getPreciounitario());
        return repository.save(nuevoDetallepedido);
    }

    public Detallepedido edit(int id, Detallepedido entity) {
        Detallepedido existenteDetallepedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detallepedido no encontrado: " + id));
        existenteDetallepedido.setCantidad(entity.getCantidad());
        existenteDetallepedido.setPreciounitario(entity.getPreciounitario());
        return repository.save(existenteDetallepedido);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

}
