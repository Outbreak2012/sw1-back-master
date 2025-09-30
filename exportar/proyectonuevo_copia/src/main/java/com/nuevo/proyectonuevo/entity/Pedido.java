package com.nuevo.proyectonuevo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.*;
@Setter
@Getter
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String estado;

    public double total;

    public Date fechapedido;


    @ManyToMany
    @JoinTable(
        name = "pedido_usuario",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnore
    private Set<Usuario> usuarios;

    @ManyToMany(mappedBy = "pedidos")
    @JsonIgnore
    private Set<Pago> pagos;

    @ManyToMany(mappedBy = "pedidos")
    @JsonIgnore
    private Set<Direccion> direccions;

    @ManyToMany(mappedBy = "pedidos")
    @JsonIgnore
    private Set<Detallepedido> detallepedidos;

}
