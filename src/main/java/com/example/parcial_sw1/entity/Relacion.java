package com.example.parcial_sw1.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "relacion")
@JsonIgnoreProperties({"tablaSource"})
public class Relacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String detalle;
    private String multtarget;
    private String multsource;
    private String targetName;
    private String sourceName;
    private String targetArgs;
    private String sourceArgs;

    @ManyToOne
    @JoinColumn(name = "tabla_source")
    private Tabla tablaSource;

    @ManyToOne
    @JoinColumn(name = "tabla_target")
    private Tabla tablaTarget;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private Tipo tipo;



    @Override   
     public String toString() {
        return "Relacion{" +

                "id=" + id +
                ", detalle='" + detalle + '\'' +
                ", multtarget='" + multtarget + '\'' +
                ", multsource='" + multsource + '\'' +
                ", targetName='" + targetName + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", targetArgs='" + targetArgs + '\'' +
                ", sourceArgs='" + sourceArgs + '\'' +
                ", tablaSourceId=" + (tablaSource != null ? tablaSource.getId() : "null") +
                ", tablaTargetId=" + (tablaTarget != null ? tablaTarget.getId() : "null") +
                ", tipoId=" + (tipo != null ? tipo.getId() : "null") +
                '}';
    
    } 

}
