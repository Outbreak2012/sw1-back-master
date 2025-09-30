package com.nuevo.proyectonuevo.service;

import java.util.List;
import java.util.Optional;
import com.nuevo.proyectonuevo.entity.*;
import com.nuevo.proyectonuevo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    

    public List<Pedido> findAll() {
        return repository.findAll();
    }

    public Optional<Pedido> findById(int id) {
        return repository.findById(id);
    }

    public Pedido save(Pedido entity) {
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setEstado(entity.getEstado());
        nuevoPedido.setTotal(entity.getTotal());
        nuevoPedido.setFechapedido(entity.getFechapedido());
        return repository.save(nuevoPedido);
    }

    public Pedido edit(int id, Pedido entity) {
        Pedido existentePedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
        existentePedido.setEstado(entity.getEstado());
        existentePedido.setTotal(entity.getTotal());
        existentePedido.setFechapedido(entity.getFechapedido());
        return repository.save(existentePedido);
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

}
