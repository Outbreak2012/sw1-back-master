package com.example.parcial_sw1.config;

import com.example.parcial_sw1.entity.*;
import com.example.parcial_sw1.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

@Component("ProyectoSeeder")
@DependsOn({ "RoleSeeder" })
public class ProyectoSeeder implements CommandLineRunner, Ordered {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TipoRepo tipoRepo;

    @Autowired
    private RelacionRepo relacionRepo;

    @Autowired
    private AtributoRepo atributoRepo;

    @Autowired
    private TipoDatoRepo tipoDatoRepo;

    @Autowired
    private ScopeRepo scopeRepo;

    @Autowired
    private TablaRepo tablaRepo;

    @Autowired
    private ProyectoRepo proyectoRepo;

    @Autowired
    private UsersRepo ourUsersRepo;

    @Autowired
    private RoleRepo rolRepository;

    @Autowired
    private ColaboradorRepo colaboradorRepo;
    @Autowired
    private InvitacionRepo invitacionRepo;

    @Override
    public int getOrder() {
        return 6;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Poblar Scope
        if (scopeRepo.findAll().isEmpty()) {

            Scope publicScope = new Scope();
            publicScope.setNombre("public");
            scopeRepo.save(publicScope);

            Scope privateScope = new Scope();
            privateScope.setNombre("private");
            scopeRepo.save(privateScope);
        }

        if (tipoDatoRepo.findAll().isEmpty()) {
            Tipo_Dato tipoDatoString = new Tipo_Dato();
            tipoDatoString.setNombre("string");
            tipoDatoRepo.save(tipoDatoString);

            Tipo_Dato tipoDatoInteger = new Tipo_Dato();
            tipoDatoInteger.setNombre("int");
            tipoDatoRepo.save(tipoDatoInteger);

            Tipo_Dato tipoDatoChar = new Tipo_Dato();
            tipoDatoChar.setNombre("char");
            tipoDatoRepo.save(tipoDatoChar);

            Tipo_Dato tipoDatoBoolean = new Tipo_Dato();
            tipoDatoBoolean.setNombre("boolean");
            tipoDatoRepo.save(tipoDatoBoolean);

            Tipo_Dato tipoDatoByte = new Tipo_Dato();
            tipoDatoByte.setNombre("byte");
            tipoDatoRepo.save(tipoDatoByte);

            Tipo_Dato tipoDatoDouble = new Tipo_Dato();
            tipoDatoDouble.setNombre("double");
            tipoDatoRepo.save(tipoDatoDouble);

            Tipo_Dato tipoDatoDate = new Tipo_Dato();
            tipoDatoDate.setNombre("date");
            tipoDatoRepo.save(tipoDatoDate);

            Tipo_Dato tipoDatoShort = new Tipo_Dato();
            tipoDatoShort.setNombre("short");
            tipoDatoRepo.save(tipoDatoShort);

            Tipo_Dato tipoDatoTime = new Tipo_Dato();
            tipoDatoTime.setNombre("time");
            tipoDatoRepo.save(tipoDatoTime);

        }
        // Poblar Tipo_Dato

        // Poblar Tipo
        if (tipoRepo.findByNombre("asociacion").isEmpty()) {
            Tipo tipoasociacion = new Tipo();
            tipoasociacion.setNombre("asociacion");
            tipoRepo.save(tipoasociacion);

            Tipo tipoagregacion = new Tipo();
            tipoagregacion.setNombre("agregacion");
            tipoRepo.save(tipoagregacion);

            Tipo tipocomposicion = new Tipo();
            tipocomposicion.setNombre("composicion");
            tipoRepo.save(tipocomposicion);

            Tipo tipoherencia = new Tipo();
            tipoherencia.setNombre("herencia");
            tipoRepo.save(tipoherencia);
        }

        Role role1 = rolRepository.findById(1).orElseThrow(() -> new RuntimeException("Role not found"));
        Role role2 = rolRepository.findById(2).orElseThrow(() -> new RuntimeException("Role not found"));

        if (!ourUsersRepo.findByEmail("adm@gmail.com").isPresent()) {
            OurUsers usuario1 = new OurUsers();
            usuario1.setName("Gustavo");
            usuario1.setEmail("adm@gmail.com");
            usuario1.setUrlAvatar("assets/imagenes/avatars/avatar1.jpg");
            usuario1.setPassword(passwordEncoder.encode("12345678"));
            usuario1.setRoles(Arrays.asList(role1, role2));
            ourUsersRepo.save(usuario1);
        }
        if (!ourUsersRepo.findByEmail("gustavo@gmail.com").isPresent()) {

            OurUsers usuario2 = new OurUsers();
            usuario2.setName("Gustavo");
            usuario2.setEmail("gustavo@gmail.com");
            usuario2.setUrlAvatar("assets/imagenes/avatars/avatar2.jpg");
            usuario2.setPassword(passwordEncoder.encode("12345678"));
            usuario2.setRoles(Arrays.asList(role1, role2));
            ourUsersRepo.save(usuario2);
        }

        // Poblar Proyecto
        OurUsers creador = ourUsersRepo.findById(1).orElseThrow(() -> new Exception("Creador no encontrado"));
        Proyecto proyecto = new Proyecto();
        proyecto.setTitulo("Proyecto de Prueba");
        proyecto.setDescripcion("Descripción del proyecto de prueba");
        proyecto.setUrlPreview("assets/imagenes/previews/preview2.jpg");
        proyecto.setCreador(creador);
        proyectoRepo.save(proyecto);

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        /* OurUsers colaboradorusuario = ourUsersRepo.findById(2)
                .orElseThrow(() -> new Exception("Colaborador no encontrado")); */
        /* Proyecto proyecto3 = proyectoRepo.findById(1).orElseThrow(() -> new Exception("Proyecto no encontrado")); */
        ;
       /*  Colaborador colaborador = new Colaborador();
        colaborador.setProyecto(proyecto3);
        colaborador.setUsuario(colaboradorusuario);
        colaborador.setPermiso("editar");
        colaborador.setFecha(LocalDate.parse("21-06-2024", formatter));
        colaboradorRepo.save(colaborador); */

       /*  if (invitacionRepo.findByEmail("gustavo25@gmail.com").isEmpty()) {
            Invitacion invitacion = new Invitacion();
            invitacion.setProyecto(proyecto3);
            invitacion.setPermiso("editar");
            invitacion.setEmail("gustavo25@gmail.com");
            invitacionRepo.save(invitacion);
        } */

        // Poblar Tablas

        if (tablaRepo.findAll().isEmpty()) {
            Tabla tablaSource = new Tabla();
            tablaSource.setName("TablaSource");
            tablaSource.setPosicion_x(100.0);
            tablaSource.setPosicion_y(200.0);
            tablaSource.setTabcolor("#FF0000");
            tablaSource.setProyecto(proyecto);
            tablaRepo.save(tablaSource);

            Tabla tablaTarget = new Tabla();
            tablaTarget.setName("TablaTarget");
            tablaTarget.setPosicion_x(300.0);
            tablaTarget.setPosicion_y(400.0);
            tablaTarget.setTabcolor("#00FF00");
            tablaTarget.setProyecto(proyecto);
            tablaRepo.save(tablaTarget);

        }

        // Poblar Relacion
        if (relacionRepo.findAll().isEmpty()) {
            Tipo tipoasociacion = tipoRepo.findByNombre("asociacion")
                    .orElseThrow(() -> new RuntimeException("Tipo 'asociacion' no encontrado"));

            Tabla tablaSource = tablaRepo.findByName( "TablaSource")
                    .orElseThrow(() -> new RuntimeException("Tabla Source no encontrada"));
            Tabla tablaTarget = tablaRepo.findByName( "TablaTarget")
                    .orElseThrow(() -> new RuntimeException("Tabla Target no encontrada"));

            Relacion relacion = new Relacion();
            relacion.setDetalle("Source to Target");
            relacion.setMultsource("1..*");
            relacion.setMulttarget("1");
            relacion.setSourceName("top");
            relacion.setTargetName("bottom");
            relacion.setSourceArgs("{ dx: -40 }");
            relacion.setTargetArgs("{ dy: -30 }");
            relacion.setTipo(tipoasociacion);
            relacion.setTablaSource(tablaSource);
            relacion.setTablaTarget(tablaTarget);
            relacionRepo.save(relacion);
        }

        // Poblar Atributos
        if (atributoRepo.findAll().isEmpty()) {
            Tipo_Dato tipoDatoString = tipoDatoRepo.findByNombre("string")
                    .orElseThrow(() -> new RuntimeException("Tipo_Dato 'string' no encontrado"));
            Tipo_Dato tipoDatoInteger = tipoDatoRepo.findByNombre("int")
                    .orElseThrow(() -> new RuntimeException("Tipo_Dato 'int' no encontrado"));
            Tabla tablaSource = tablaRepo.findByProyectoAndName(proyecto, "TablaSource")
                    .orElseThrow(() -> new RuntimeException("Tabla Source no encontrada"));
            Scope publicScope = scopeRepo.findByNombre("public")
                    .orElseThrow(() -> new RuntimeException("Scope 'public' no encontrado"));
            Scope privateScope = scopeRepo.findByNombre("private")
                    .orElseThrow(() -> new RuntimeException("Scope 'private' no encontrado"));
            Atributo atributo1 = new Atributo();
            atributo1.setNombre("Nombre");
            atributo1.setNulleable(false);
            atributo1.setPk(true);
            atributo1.setScope(publicScope);
            atributo1.setTipoDato(tipoDatoString);
            atributo1.setTabla(tablaSource);
            atributoRepo.save(atributo1);

            Atributo atributo2 = new Atributo();
            atributo2.setNombre("Edad");
            atributo2.setNulleable(true);
            atributo2.setPk(false);
            atributo2.setScope(privateScope);
            atributo2.setTipoDato(tipoDatoInteger);
            atributo2.setTabla(tablaSource);
            atributoRepo.save(atributo2);
        }

        System.out.println("Database seed complete!");
    }
}
