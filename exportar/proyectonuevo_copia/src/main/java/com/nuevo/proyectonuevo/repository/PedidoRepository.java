package com.nuevo.proyectonuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nuevo.proyectonuevo.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

}
