package com.nuevo.proyectonuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nuevo.proyectonuevo.entity.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

}
