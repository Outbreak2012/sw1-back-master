package com.example.parcial_sw1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tabla")
@JsonIgnoreProperties({"proyecto", "relacionesTarget"})
public class Tabla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private Double posicion_x;
    private Double posicion_y;
    private String tabcolor;

    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    @OneToMany(mappedBy = "tablaSource", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Relacion> relacionesSource;

    @OneToMany(mappedBy = "tablaTarget", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Relacion> relacionesTarget;

    @OneToMany(mappedBy = "tabla", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atributo> atributos;


    @Override
    public String toString() {
        return "Tabla{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", posicion_x=" + posicion_x +
                ", posicion_y=" + posicion_y +
                ", relacionesSourceCount=" + (relacionesSource != null ? relacionesSource.size() : 0) +
                ", relacionesTargetCount=" + (relacionesTarget != null ? relacionesTarget.size() : 0) +
                ", relacionesSource=" + (relacionesSource != null ? relacionesSource : "null") +
                ", relacionesTarget=" + (relacionesTarget != null ? relacionesTarget : "null") +
                ", tabcolor='" + tabcolor + '\'' +
                ", proyectoId=" + (proyecto != null ? proyecto.getId() : "null") +
                ", atributosCount=" + (atributos != null ? atributos.size() : 0) +
                '}';
    }
}
