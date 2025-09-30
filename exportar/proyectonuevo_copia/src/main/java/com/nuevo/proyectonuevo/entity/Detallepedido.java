package com.nuevo.proyectonuevo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.*;
@Setter
@Getter
@Entity
@Table(name = "detallepedido")
public class Detallepedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public int cantidad;

    public double preciounitario;


    @ManyToMany
    @JoinTable(
        name = "detallepedido_pedido",
        joinColumns = @JoinColumn(name = "detallepedido_id"),
        inverseJoinColumns = @JoinColumn(name = "pedido_id")
    )
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @JsonIgnore
    private Set<Pedido> pedidos;

}
