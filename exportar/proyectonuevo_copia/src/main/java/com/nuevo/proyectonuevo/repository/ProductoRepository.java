package com.nuevo.proyectonuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nuevo.proyectonuevo.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
