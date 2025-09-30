package com.example.parcial_sw1.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "proyecto")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String descripcion;
    private String titulo;
    private String urlPreview;

    @ManyToOne
    @JoinColumn(name = "creador_id")
    private OurUsers creador;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Colaborador> colaboradores;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tabla> tablas;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invitacion> invitaciones;


   @Override
    public String toString() {
        return "Proyecto{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", titulo='" + titulo + '\'' +
                ", urlPreview='" + urlPreview + '\'' +
                ", creador=" + (creador != null ? creador.getName() : "null") +
                ", tablasCount=" + (tablas != null ? tablas.size() : 0) +
                ", tablas=" + (tablas != null ? tablas : "null") +
                ", colaboradoresCount=" + (colaboradores != null ? colaboradores.size() : 0) +
                ", invitacionesCount=" + (invitaciones != null ? invitaciones.size() : 0) +
                '}';
    }
    
}
