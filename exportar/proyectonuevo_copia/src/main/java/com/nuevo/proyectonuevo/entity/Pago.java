package com.nuevo.proyectonuevo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.*;
@Setter
@Getter
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String estado;

    public String metodo;

    public double monto;

    public Date fechapago;


    @ManyToMany
    @JoinTable(
        name = "pago_pedido",
        joinColumns = @JoinColumn(name = "pago_id"),
        inverseJoinColumns = @JoinColumn(name = "pedido_id")
    )
    @JsonIgnore
    private Set<Pedido> pedidos;

}
