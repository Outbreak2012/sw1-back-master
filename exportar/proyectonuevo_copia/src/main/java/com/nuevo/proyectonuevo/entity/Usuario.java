package com.nuevo.proyectonuevo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.*;
@Setter
@Getter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String contrasena;

    public String email;

    public String nombre;

    public Date fecharegistro;


    @ManyToMany
    @JoinTable(
        name = "usuario_pedido",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "pedido_id")
    )
    @JsonIgnore
    private Set<Pedido> pedidos;

    @ManyToMany
    @JoinTable(
        name = "usuario_direccion",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "direccion_id")
    )
    @JsonIgnore
    private Set<Direccion> direccions;

    @ManyToMany(mappedBy = "usuarios")
    @JsonIgnore
    private Set<Pedido> pedidos;

}
