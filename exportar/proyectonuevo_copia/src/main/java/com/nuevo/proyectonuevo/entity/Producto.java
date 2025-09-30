package com.nuevo.proyectonuevo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.*;
@Setter
@Getter
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String imagenurl;

    public String descripcion;

    public String nombre;

    public double peso;

    public double precio;


    @ManyToMany
    @JoinTable(
        name = "producto_inventario",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "inventario_id")
    )
    @JsonIgnore
    private Set<Inventario> inventarios;

    @ManyToMany
    @JoinTable(
        name = "producto_detallepedido",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "detallepedido_id")
    )
    @JsonIgnore
    private Set<Detallepedido> detallepedidos;

}
