package com.nuevo.proyectonuevo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.*;
@Setter
@Getter
@Entity
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String pais;

    public String codigopostal;

    public String provincia;

    public String ciudad;

    public String calle;


    @ManyToMany
    @JoinTable(
        name = "direccion_pedido",
        joinColumns = @JoinColumn(name = "direccion_id"),
        inverseJoinColumns = @JoinColumn(name = "pedido_id")
    )
    @JsonIgnore
    private Set<Pedido> pedidos;

}
