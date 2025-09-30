package com.nuevo.proyectonuevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nuevo.proyectonuevo.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

}
