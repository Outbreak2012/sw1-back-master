package com.nuevo.proyectonuevo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.*;
@Setter
@Getter
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String ubicacion;

    public int cantidaddisponible;


}
