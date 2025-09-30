package com.nuevo.proyectonuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nuevo.proyectonuevo.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
