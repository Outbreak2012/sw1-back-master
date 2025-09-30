package com.example.parcial_sw1.service;

import com.example.parcial_sw1.entity.Atributo;
import com.example.parcial_sw1.entity.Proyecto;
import com.example.parcial_sw1.entity.Relacion;
import com.example.parcial_sw1.entity.Tabla;
import com.example.parcial_sw1.repository.ProyectoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.nio.file.*;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ORMService {
    @Autowired
    private ProyectoRepo proyectoRepo;

    public String generarEntidadesEnCopia(int proyectoId) throws Exception {

        try {
            Proyecto proyecto = proyectoRepo.findById(proyectoId)
                    .orElseThrow(() -> new Exception("Proyecto no encontrado"));
            System.out.println("llega a Proyecto encontrado: " + proyecto.toString());
            String basePath = System.getProperty("user.dir") + "/exportar/";
            String rutaOriginal = basePath + "proyectonuevo";
            String rutaZip = basePath + "zips";
            String rutaCopia = basePath + "proyectonuevo_copia";
            System.out.println("llega a Ruta Original: " + rutaOriginal);
            copiarProyecto(rutaOriginal, rutaCopia);
            System.out.println("llega a Proyecto copiado a: " + rutaCopia);
            String rutaBaseEntity = rutaCopia + "/src/main/java/com/nuevo/proyectonuevo/entity";
            String rutaBaseRepository = rutaCopia + "/src/main/java/com/nuevo/proyectonuevo/repository";
            String rutaBaseService = rutaCopia + "/src/main/java/com/nuevo/proyectonuevo/service";
            String rutaBaseController = rutaCopia + "/src/main/java/com/nuevo/proyectonuevo/controller";
            crearDirectorioProyecto(rutaBaseEntity);
            crearDirectorioProyecto(rutaBaseRepository);
            crearDirectorioProyecto(rutaBaseService);
            crearDirectorioProyecto(rutaBaseController);
            List<Tabla> tablas = proyecto.getTablas();
            System.out.println("llega aca Tablas encontradas: " + tablas.size());
            for (Tabla tabla : tablas) {
                String tableName = tabla.getName();
                String formattedTableName = tableName.substring(0, 1).toUpperCase()
                        + tableName.substring(1).toLowerCase();
                generarEntidad(tabla, rutaBaseEntity, formattedTableName);
                generarRepository(rutaBaseRepository, formattedTableName);
                generarService(rutaBaseService, formattedTableName, tabla);
                generarController(rutaBaseController, formattedTableName);
            }
            comprimirDirectorio(rutaCopia, rutaZip);
            return rutaZip;
        } catch (Exception e) {
            System.out.println("Error al generar entidades: asdsa" + e.getMessage());
            throw new Exception("Error al generar entidades: " + e.getMessage());
        }

    }

    public void comprimirDirectorio(String rutaDirectorio, String rutaZip) throws IOException {
        Path dir = Paths.get(rutaDirectorio);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(rutaZip))) {
            Files.walk(dir).forEach(sourcePath -> {
                String zipEntryName = dir.relativize(sourcePath).toString();
                try {
                    if (Files.isDirectory(sourcePath)) {
                        if (!zipEntryName.isEmpty()) {
                            zos.putNextEntry(new ZipEntry(zipEntryName + "/"));
                        }
                    } else {
                        zos.putNextEntry(new ZipEntry(zipEntryName));
                        Files.copy(sourcePath, zos);
                    }
                    zos.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void copiarProyecto(String rutaOriginal, String rutaDestino) throws IOException {
        try {
            Path origen = Paths.get(rutaOriginal);
            Path destino = Paths.get(rutaDestino);
            System.out.println("llega a copiar proyecto de: " + rutaOriginal + " a: " + rutaDestino);
            if (Files.exists(destino)) {
                System.out.println("Intentando eliminar directorio destino: " + destino);
                try {
                    eliminarDirectorio(destino);
                    System.out.println("Directorio destino eliminado correctamente");
                } catch (IOException ex) {
                    System.out.println("Error al eliminar directorio destino: " + ex.getMessage());
                    ex.printStackTrace();
                    throw ex;
                }
            }
            System.out.println("Intentando crear directorio destino: " + destino);
            try {
                Files.createDirectories(destino);
                System.out.println("Directorio destino creado correctamente");
            } catch (IOException ex) {
                System.out.println("Error al crear directorio destino: " + ex.getMessage());
                ex.printStackTrace();
                throw ex;
            }
            System.out.println("Iniciando copiado de archivos...");
            Files.walk(origen).forEach(sourcePath -> {
                try {
                    Path destinoPath = destino.resolve(origen.relativize(sourcePath));
                    if (Files.isDirectory(sourcePath)) {
                        if (!Files.exists(destinoPath)) {
                            Files.createDirectories(destinoPath);
                        }
                    } else {
                        Files.copy(sourcePath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    System.out.println("Error al copiar archivo: " + sourcePath + " -> " + e.getMessage());
                    e.printStackTrace();
                }
            });
            System.out.println("Copiado de archivos finalizado");
        } catch (IOException e) {
            System.out.println("Error general al copiar proyecto: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void eliminarDirectorio(Path directorio) throws IOException {
        Files.walkFileTree(directorio, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void generarEntidad(Tabla tabla, String rutaBase, String formattedTableName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.nuevo.proyectonuevo.entity;\n\n");
        sb.append("import jakarta.persistence.*;\n");
        sb.append("import org.hibernate.annotations.Cascade;\n");
        sb.append("import com.fasterxml.jackson.annotation.JsonIgnore;\n");
        sb.append("import lombok.*;\n");
        sb.append("import java.util.*;\n");
        sb.append("@Setter\n");
        sb.append("@Getter\n");
        sb.append("@Entity\n");
        sb.append("@Table(name = \"").append(tabla.getName().toLowerCase()).append("\")\n");
        sb.append("public class ").append(formattedTableName).append(" {\n\n");
        sb.append("    @Id\n");
        sb.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
        sb.append("    private int id;\n");
        sb.append("\n");
        for (Atributo atributo : tabla.getAtributos()) {
            String tipoName = atributo.getTipoDato().getNombre();
            if (tipoName.equals("string") || tipoName.equals("date")) {
                tipoName = tipoName.substring(0, 1).toUpperCase() + tipoName.substring(1).toLowerCase();
            }
            sb.append("    ").append(atributo.getScope().getNombre().toLowerCase()).append(" ")
                    .append(tipoName).append(" ").append(atributo.getNombre().toLowerCase()).append(";\n\n");
        }

        sb.append("\n");

        System.out.println("Relaciones Target de " + tabla.getRelacionesTarget());
        for (Relacion relacion : tabla.getRelacionesTarget()) {
            
            if (esRelacionManyToOne(relacion)) {
                String tableSourceName = relacion.getTablaSource().getName().toLowerCase();
                String nombrePropiedad;
                String nombreColumna;
                
                if (esRelacionReflexiva(relacion)) {
                    // Relación reflexiva - misma tabla
                    nombrePropiedad = "padre";
                    nombreColumna = "padre_id"; // Columna específica para jerarquías
                } else {
                    // Relación normal
                    nombrePropiedad = tableSourceName;
                    nombreColumna = tableSourceName + "_id";
                }
                
                sb.append("    @ManyToOne\n");
                
                // Agregar nullable según multiplicidad
                if (esOpcional(relacion.getMultsource())) {
                    sb.append("    @JoinColumn(name = \"").append(nombreColumna).append("\", nullable = true)\n");
                } else {
                    sb.append("    @JoinColumn(name = \"").append(nombreColumna).append("\", nullable = false)\n");
                }
                String formattedTableSourceName = tableSourceName.substring(0, 1).toUpperCase()
                        + tableSourceName.substring(1);
                sb.append("    private ").append(formattedTableSourceName).append(" ").append(nombrePropiedad)
                        .append(";\n\n");

            } else if (esRelacionOneToMany(relacion)) {
                String tableSourceName = relacion.getTablaSource().getName().toLowerCase();
                String tipoRelacion = relacion.getTipo().getNombre();
                
                // Configurar nombre de propiedad según si es reflexiva o no
                String nombrePropiedad;
                String mappedBy;
                
                if (esRelacionReflexiva(relacion)) {
                    // Relación reflexiva - misma tabla (ej: Employee -> Manager)
                    nombrePropiedad = "hijos";
                    mappedBy = "padre"; // En ManyToOne se llamará "padre"
                } else {
                    // Relación normal
                    nombrePropiedad = tableSourceName + "s";
                    mappedBy = tabla.getName().toLowerCase();
                }
                
                // Generar anotación según el tipo de relación
                sb.append("    @OneToMany(mappedBy = \"").append(mappedBy).append("\"");
                
                if (tipoRelacion.equals("composicion")) {
                    // Composición: cascada completa y orphanRemoval
                    sb.append(", cascade = CascadeType.ALL, orphanRemoval = true");
                } else if (tipoRelacion.equals("agregacion")) {
                    // Agregación: solo persist y merge, sin orphanRemoval
                    sb.append(", cascade = {CascadeType.PERSIST, CascadeType.MERGE}");
                } else {
                    // Asociación: solo persist
                    sb.append(", cascade = CascadeType.PERSIST");
                }
                
                sb.append(")\n");
                String formattedTableSourceName = tableSourceName.substring(0, 1).toUpperCase()
                        + tableSourceName.substring(1);
                sb.append("    @JsonIgnore\n");
                sb.append("    private List<").append(formattedTableSourceName).append("> ").append(nombrePropiedad)
                        .append(";\n\n");
                        
            } else if (esRelacionManyToMany(relacion)) {
                String tableSourceName = relacion.getTablaSource().getName().toLowerCase();
                String tipoRelacion = relacion.getTipo().getNombre();
                
                // Configurar nombre de tabla intermedia
                String nombreTablaIntermedia = tabla.getName().toLowerCase() + "_" + tableSourceName;
                String nombrePropiedad = tableSourceName + "s";
                
                sb.append("    @ManyToMany\n");
                sb.append("    @JoinTable(\n");
                sb.append("        name = \"").append(nombreTablaIntermedia).append("\",\n");
                sb.append("        joinColumns = @JoinColumn(name = \"").append(tabla.getName().toLowerCase()).append("_id\"),\n");
                sb.append("        inverseJoinColumns = @JoinColumn(name = \"").append(tableSourceName).append("_id\")\n");
                sb.append("    )\n");
                
                // Configurar cascade según tipo de relación
                if (tipoRelacion.equals("composicion")) {
                    sb.append("    @Cascade({org.hibernate.annotations.CascadeType.ALL})\n");
                } else if (tipoRelacion.equals("agregacion")) {
                    sb.append("    @Cascade({org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.MERGE})\n");
                }
                
                String formattedTableSourceName = tableSourceName.substring(0, 1).toUpperCase()
                        + tableSourceName.substring(1);
                sb.append("    @JsonIgnore\n");
                sb.append("    private Set<").append(formattedTableSourceName).append("> ").append(nombrePropiedad)
                        .append(";\n\n");
            }
        }

        for (Relacion relacion : tabla.getRelacionesSource()) {
            if (esRelacionOneToManySource(relacion)) {
                String tableTargetName = relacion.getTablaTarget().getName().toLowerCase();
                String tipoRelacion = relacion.getTipo().getNombre();
                
                String nombrePropiedad;
                String mappedBy;
                
                if (esRelacionReflexiva(relacion)) {
                    // Relación reflexiva - misma tabla
                    nombrePropiedad = "hijos";
                    mappedBy = "padre";
                } else {
                    // Relación normal
                    nombrePropiedad = tableTargetName + "s";
                    mappedBy = tabla.getName().toLowerCase();
                }
                
                sb.append("    @OneToMany(mappedBy = \"").append(mappedBy).append("\"");
                
                if (tipoRelacion.equals("composicion")) {
                    sb.append(", cascade = CascadeType.ALL, orphanRemoval = true");
                } else if (tipoRelacion.equals("agregacion")) {
                    sb.append(", cascade = {CascadeType.PERSIST, CascadeType.MERGE}");
                } else {
                    sb.append(", cascade = CascadeType.PERSIST");
                }
                
                sb.append(")\n");
                String formattedTableTargetName = tableTargetName.substring(0, 1).toUpperCase()
                        + tableTargetName.substring(1);
                sb.append("    @JsonIgnore\n");
                sb.append("    private List<").append(formattedTableTargetName).append("> ").append(nombrePropiedad)
                        .append(";\n\n");
                        
            } else if (esRelacionManyToMany(relacion)) {
                String tableTargetName = relacion.getTablaTarget().getName().toLowerCase();
                String tipoRelacion = relacion.getTipo().getNombre();
                
                // Para evitar duplicar la relación, solo generar en una dirección
                // Generar solo si esta tabla tiene ID menor que la tabla target
                if (tabla.getId() < relacion.getTablaTarget().getId()) {
                    String nombreTablaIntermedia = tabla.getName().toLowerCase() + "_" + tableTargetName;
                    String nombrePropiedad = tableTargetName + "s";
                    
                    sb.append("    @ManyToMany(mappedBy = \"").append(tabla.getName().toLowerCase()).append("s\")\n");
                    
                    String formattedTableTargetName = tableTargetName.substring(0, 1).toUpperCase()
                            + tableTargetName.substring(1);
                    sb.append("    @JsonIgnore\n");
                    sb.append("    private Set<").append(formattedTableTargetName).append("> ").append(nombrePropiedad)
                            .append(";\n\n");
                }

            } else if (esRelacionManyToOneSource(relacion)) {
                String tableTargetName = relacion.getTablaTarget().getName().toLowerCase();
                String nombrePropiedad;
                String nombreColumna;
                
                if (esRelacionReflexiva(relacion)) {
                    // Relación reflexiva - misma tabla
                    nombrePropiedad = "padre";
                    nombreColumna = "padre_id";
                } else {
                    // Relación normal
                    nombrePropiedad = tableTargetName;
                    nombreColumna = tableTargetName + "_id";
                }
                
                sb.append("    @ManyToOne\n");
                
                // Agregar nullable según multiplicidad
                if (esOpcional(relacion.getMulttarget())) {
                    sb.append("    @JoinColumn(name = \"").append(nombreColumna).append("\", nullable = true)\n");
                } else {
                    sb.append("    @JoinColumn(name = \"").append(nombreColumna).append("\", nullable = false)\n");
                }
                String formattedTableTargetName = tableTargetName.substring(0, 1).toUpperCase()
                        + tableTargetName.substring(1);
                sb.append("    private ").append(formattedTableTargetName).append(" ").append(nombrePropiedad)
                        .append(";\n\n");

            } else if (esRelacionOneToOneSource(relacion)) {
                // Relación uno a uno
                String tableTargetName = relacion.getTablaTarget().getName().toLowerCase();
                sb.append("    @OneToOne\n");
                sb.append("    @JoinColumn(name = \"").append(tableTargetName).append("_id\")\n"); // Define la columna
                                                                                                   // que actúa como FK
                String formattedTableTargetName = tableTargetName.substring(0, 1).toUpperCase()
                        + tableTargetName.substring(1);
                sb.append("    private ").append(formattedTableTargetName).append(" ").append(tableTargetName)
                        .append(";\n\n");
            }
        }
        sb.append("}\n");
        String rutaArchivo = rutaBase + "/" + formattedTableName + ".java";
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generarRepository(String rutaBase, String formattedTableName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.nuevo.proyectonuevo.repository;\n\n");
        sb.append("import org.springframework.data.jpa.repository.JpaRepository;\n");
        sb.append("import com.nuevo.proyectonuevo.entity.").append(formattedTableName).append(";\n\n");
        sb.append("public interface ").append(formattedTableName).append("Repository extends JpaRepository<")
                .append(formattedTableName).append(", Integer> {\n\n");
        sb.append("}\n");

        String rutaArchivo = rutaBase + "/" + formattedTableName + "Repository.java";
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write(sb.toString());
        }
    }

    private void generarService(String rutaBase, String formattedTableName, Tabla tabla) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.nuevo.proyectonuevo.service;\n\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Optional;\n");
        sb.append("import com.nuevo.proyectonuevo.entity.*;\n");
        sb.append("import com.nuevo.proyectonuevo.repository.*;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.stereotype.Service;\n\n");
        sb.append("@Service\n");
        sb.append("public class ").append(formattedTableName).append("Service {\n\n");
        sb.append("    @Autowired\n");
        sb.append("    private ").append(formattedTableName).append("Repository repository;\n\n");
        for (Relacion relacion : tabla.getRelacionesTarget()) {
            if (esRelacionManyToOne(relacion)) {
                String tableSourceName = relacion.getTablaSource().getName().toLowerCase();
                String formattedTableTargetName = tableSourceName.substring(0, 1).toUpperCase()
                        + tableSourceName.substring(1);
                sb.append("    @Autowired\n");
                sb.append("    private ").append(formattedTableTargetName).append("Repository ").append(tableSourceName)
                        .append("Repository ").append(";\n\n");
            }
        }
        for (Relacion relacion : tabla.getRelacionesSource()) {
            if (esRelacionManyToOneSource(relacion) || esRelacionOneToOneSource(relacion)) {
                String tableTargetName = relacion.getTablaTarget().getName().toLowerCase();
                String formattedTableTargetName = tableTargetName.substring(0, 1).toUpperCase()
                        + tableTargetName.substring(1);
                sb.append("    @Autowired\n");
                sb.append("    private ").append(formattedTableTargetName).append("Repository ").append(tableTargetName)
                        .append("Repository ").append(";\n\n");
            }
        }
        sb.append("    \n\n");
        sb.append("    public List<").append(formattedTableName).append("> findAll() {\n");
        sb.append("        return repository.findAll();\n");
        sb.append("    }\n\n");
        sb.append("    public Optional<").append(formattedTableName).append("> findById(int id) {\n");
        sb.append("        return repository.findById(id);\n");
        sb.append("    }\n\n");
        sb.append("    public ").append(formattedTableName).append(" save(").append(formattedTableName)
                .append(" entity) {\n");
        sb.append("        ").append(formattedTableName).append(" nuevo").append(formattedTableName).append(" = new ")
                .append(formattedTableName).append("();\n");
        for (Atributo atributo : tabla.getAtributos()) {
            String nombreAtributo = atributo.getNombre();
            String nombreFormateado = nombreAtributo.substring(0, 1).toUpperCase()
                    + nombreAtributo.substring(1).toLowerCase();
            sb.append("        nuevo").append(formattedTableName).append(".set").append(nombreFormateado)
                    .append("(entity.get").append(nombreFormateado).append("());\n");

        }
        for (Relacion relacion : tabla.getRelacionesTarget()) {
            if (esRelacionManyToOne(relacion)) {
                String tableSourceName = relacion.getTablaSource().getName().toLowerCase();
                String formattedTableSourceName = tableSourceName.substring(0, 1).toUpperCase()
                        + tableSourceName.substring(1);
                sb.append("        ").append(formattedTableSourceName).append(" ").append(tableSourceName).append(" = ")
                        .append(tableSourceName).append("Repository ").append(".findById(entity.get")
                        .append(formattedTableSourceName).append("().getId())\n");
                sb.append("                .orElseThrow(() -> new RuntimeException(\"").append(tableSourceName)
                        .append(" no encontrado: \" + entity.get").append(formattedTableSourceName)
                        .append("().getId()));\n");
                sb.append("        nuevo").append(formattedTableName).append(".set").append(formattedTableSourceName)
                        .append("(").append(tableSourceName).append(");\n");
            }
        }
        for (Relacion relacion : tabla.getRelacionesSource()) {
            if (esRelacionManyToOneSource(relacion) || esRelacionOneToOneSource(relacion)) {
                String tableTargetName = relacion.getTablaTarget().getName().toLowerCase();
                String formattedTableTargetName = tableTargetName.substring(0, 1).toUpperCase()
                        + tableTargetName.substring(1);
                sb.append("        ").append(formattedTableTargetName).append(" ").append(tableTargetName).append(" = ")
                        .append(tableTargetName).append("Repository ").append(".findById(entity.get")
                        .append(formattedTableTargetName).append("().getId())\n");
                sb.append("                .orElseThrow(() -> new RuntimeException(\"").append(tableTargetName)
                        .append(" no encontrado: \" + entity.get").append(formattedTableTargetName)
                        .append("().getId()));\n");
                sb.append("        nuevo").append(formattedTableName).append(".set").append(formattedTableTargetName)
                        .append("(").append(tableTargetName).append(");\n");
            }
        }
        sb.append("        return repository.save(nuevo").append(formattedTableName).append(");\n");
        sb.append("    }\n\n");
        sb.append("    public ").append(formattedTableName).append(" edit(int id, ").append(formattedTableName)
                .append(" entity) {\n");
        sb.append("        ").append(formattedTableName).append(" existente").append(formattedTableName)
                .append(" = repository.findById(id)\n");
        sb.append("                .orElseThrow(() -> new RuntimeException(\"").append(formattedTableName)
                .append(" no encontrado: \" + id));\n");
        for (Atributo atributo : tabla.getAtributos()) {
            String nombreAtributo = atributo.getNombre();
            String nombreFormateado = nombreAtributo.substring(0, 1).toUpperCase()
                    + nombreAtributo.substring(1).toLowerCase();
            sb.append("        existente").append(formattedTableName)
                    .append(".set").append(nombreFormateado)
                    .append("(entity.get").append(nombreFormateado).append("());\n");

        }
        for (Relacion relacion : tabla.getRelacionesTarget()) {
            if (esRelacionManyToOne(relacion)) {
                String tableSourceName = relacion.getTablaSource().getName().toLowerCase();
                String formattedTableSourceName = tableSourceName.substring(0, 1).toUpperCase()
                        + tableSourceName.substring(1);
                sb.append("        ").append(formattedTableSourceName).append(" ").append(tableSourceName).append(" = ")
                        .append(tableSourceName).append("Repository.findById(entity.get")
                        .append(formattedTableSourceName).append("().getId())\n");
                sb.append("                .orElseThrow(() -> new RuntimeException(\"").append(tableSourceName)
                        .append(" no encontrado: \" + entity.get").append(formattedTableSourceName)
                        .append("().getId()));\n");
                sb.append("        existente").append(formattedTableName).append(".set")
                        .append(formattedTableSourceName).append("(").append(tableSourceName).append(");\n");
            }
        }
        for (Relacion relacion : tabla.getRelacionesSource()) {
            if (esRelacionManyToOneSource(relacion) || esRelacionOneToOneSource(relacion)) {
                String tableTargetName = relacion.getTablaTarget().getName().toLowerCase();
                String formattedTableTargetName = tableTargetName.substring(0, 1).toUpperCase()
                        + tableTargetName.substring(1);
                sb.append("        ").append(formattedTableTargetName).append(" ").append(tableTargetName).append(" = ")
                        .append(tableTargetName).append("Repository.findById(entity.get")
                        .append(formattedTableTargetName).append("().getId())\n");
                sb.append("                .orElseThrow(() -> new RuntimeException(\"").append(tableTargetName)
                        .append(" no encontrado: \" + entity.get").append(formattedTableTargetName)
                        .append("().getId()));\n");
                sb.append("        existente").append(formattedTableName).append(".set")
                        .append(formattedTableTargetName).append("(").append(tableTargetName).append(");\n");
            }
        }
        sb.append("        return repository.save(existente").append(formattedTableName).append(");\n");
        sb.append("    }\n\n");
        sb.append("    public void deleteById(int id) {\n");
        sb.append("        repository.deleteById(id);\n");
        sb.append("    }\n\n");
        sb.append("}\n");

        String rutaArchivo = rutaBase + "/" + formattedTableName + "Service.java";
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write(sb.toString());
        }
    }

    private void generarController(String rutaBase, String formattedTableName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.nuevo.proyectonuevo.controller;\n\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Optional;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.http.HttpStatus;\n");
        sb.append("import org.springframework.http.ResponseEntity;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n");
        sb.append("import com.nuevo.proyectonuevo.entity.*;\n");
        sb.append("import com.nuevo.proyectonuevo.service.*;\n\n");
        sb.append("@RestController\n");
        sb.append("@RequestMapping(\"/api/").append(formattedTableName.toLowerCase()).append("s\")\n");
        sb.append("public class ").append(formattedTableName).append("Controller {\n\n");

        sb.append("    @Autowired\n");
        sb.append("    private ").append(formattedTableName).append("Service ").append(formattedTableName.toLowerCase())
                .append("Service;\n\n");
        sb.append("    @GetMapping(\"/get\")\n");
        sb.append("    public ResponseEntity<List<").append(formattedTableName).append(">> getAll() {\n");
        sb.append("        List<").append(formattedTableName).append("> lista = ")
                .append(formattedTableName.toLowerCase()).append("Service.findAll();\n");
        sb.append("        return ResponseEntity.ok(lista);\n");
        sb.append("    }\n\n");

        sb.append("    @GetMapping(\"/get/{id}\")\n");
        sb.append("    public ResponseEntity<").append(formattedTableName)
                .append("> getById(@PathVariable int id) {\n");
        sb.append("        Optional<").append(formattedTableName).append("> result = ")
                .append(formattedTableName.toLowerCase()).append("Service.findById(id);\n");
        sb.append(
                "        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());\n");
        sb.append("    }\n\n");

        sb.append("    @PostMapping(\"/create\")\n");
        sb.append("    public ResponseEntity<").append(formattedTableName).append("> create(@RequestBody ")
                .append(formattedTableName).append(" entity) {\n");
        sb.append("        ").append(formattedTableName).append(" nuevo = ").append(formattedTableName.toLowerCase())
                .append("Service.save(entity);\n");
        sb.append("        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);\n");
        sb.append("    }\n\n");

        sb.append("    @PutMapping(\"/edit/{id}\")\n");
        sb.append("    public ResponseEntity<").append(formattedTableName)
                .append("> edit(@PathVariable int id, @RequestBody ").append(formattedTableName).append(" entity) {\n");
        sb.append("        try {\n");
        sb.append("            ").append(formattedTableName).append(" actualizado = ")
                .append(formattedTableName.toLowerCase()).append("Service.edit(id, entity);\n");
        sb.append("            return ResponseEntity.ok(actualizado);\n");
        sb.append("        } catch (RuntimeException e) {\n");
        sb.append("            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("    @DeleteMapping(\"/delete/{id}\")\n");
        sb.append("    public ResponseEntity<Void> delete(@PathVariable int id) {\n");
        sb.append("        try {\n");
        sb.append("            ").append(formattedTableName.toLowerCase()).append("Service.deleteById(id);\n");
        sb.append("            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();\n");
        sb.append("        } catch (RuntimeException e) {\n");
        sb.append("            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();\n");
        sb.append("        }\n");
        sb.append("    }\n\n");

        sb.append("}\n");

        String rutaArchivo = rutaBase + "/" + formattedTableName + "Controller.java";
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write(sb.toString());
        }
    }

    private boolean esRelacionOneToMany(Relacion relacion) {
        String multiplicidadSource = relacion.getMultsource(); 
        String multiplicidadTarget = relacion.getMulttarget(); 

        System.out.println("Evaluando relacion OneToMany: Source=" + multiplicidadSource + ", Target=" + multiplicidadTarget);
        
        // OneToMany: uno del lado source (1, 0..1) hacia muchos del lado target (1..*, 0..*)
        return !esColeccion(multiplicidadSource) && esColeccion(multiplicidadTarget);
    }

    private boolean esRelacionManyToOne(Relacion relacion) {
        String multiplicidadSource = relacion.getMultsource(); 
        String multiplicidadTarget = relacion.getMulttarget(); 

        System.out.println("Evaluando relacion ManyToOne: Source=" + multiplicidadSource + ", Target=" + multiplicidadTarget);
        
        // ManyToOne: muchos del lado source (1..*, 0..*) hacia uno del lado target (1, 0..1)
        return esColeccion(multiplicidadTarget) && !esColeccion(multiplicidadSource);
    }
    
    private boolean esRelacionReflexiva(Relacion relacion) {
        return relacion.getTablaSource().getId() == relacion.getTablaTarget().getId();
    }
    
    private boolean esOpcional(String multiplicidad) {
        return multiplicidad.startsWith("0");
    }
    
    private boolean esColeccion(String multiplicidad) {
        return multiplicidad.contains("*") || multiplicidad.contains("..") && !multiplicidad.equals("1");
    }
    
    private boolean esRelacionManyToMany(Relacion relacion) {
        String multiplicidadSource = relacion.getMultsource();
        String multiplicidadTarget = relacion.getMulttarget();
        
        System.out.println("Evaluando relacion ManyToMany: Source=" + multiplicidadSource + ", Target=" + multiplicidadTarget);
        
        // ManyToMany: ambos lados son colecciones (1..*, 0..*)
        return esColeccion(multiplicidadSource) && esColeccion(multiplicidadTarget);
    }

    private boolean esRelacionOneToManySource(Relacion relacion) {
        String multiplicidadSource = relacion.getMultsource(); // Ej: "1"
        String multiplicidadTarget = relacion.getMulttarget(); // Ej: "*"

        return multiplicidadTarget.equals("1..*") && multiplicidadSource.equals("1");
    }

    private boolean esRelacionManyToOneSource(Relacion relacion) {
        String multiplicidadSource = relacion.getMultsource(); // Ej: "1..*"
        String multiplicidadTarget = relacion.getMulttarget(); // Ej: "1"

        return multiplicidadTarget.equals("1") && multiplicidadSource.equals("1..*");
    }

    private boolean esRelacionOneToOneSource(Relacion relacion) {
        String multiplicidadSource = relacion.getMultsource(); // Ej: "1"
        String multiplicidadTarget = relacion.getMulttarget(); // Ej: "1"

        return multiplicidadTarget.equals("1") && multiplicidadSource.equals("1");
    }

    public void crearDirectorioProyecto(String rutaDirectorio) {
        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs(); // Crea el directorio si no existe
            System.out.println("Directorio creado: " + rutaDirectorio);
        } else {
            System.out.println("Directorio ya existe: " + rutaDirectorio);
        }
    }
}
