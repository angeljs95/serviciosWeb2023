package com.egg.servicios.Controladores;


import com.egg.servicios.Entidades.Profesion;
import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.repositorios.ClienteRepositorio;
import com.egg.servicios.repositorios.ProfesionRepositorio;
import com.egg.servicios.repositorios.ProveedorRepositorio;
import com.egg.servicios.servicios.ProveedorServicio;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class PortalControlador {


    @Autowired
    private ProveedorServicio proveedorServicio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ProfesionRepositorio profesionRepositorio;


    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "El usuario o la contraseña es incorrecta");
        }

        return "form_iniciar_sesion.html";
    }


    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN' , 'PROVEEDOR')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo, Integer p,
                         @ModelAttribute("proveedoresFiltrados") ArrayList<Proveedor> proveedoresFiltrados) {
//Lista las profesiones en la barra de filtros
        List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
        //Lista las puntuaciones en la barra de filtros
        List<Proveedor> puntuacion = proveedorServicio.puntuacionP(p);
        //Lista todos los proveedores en la pag de inicio
        List<Proveedor> proveedores = proveedorServicio.listarProveedores();

        modelo.addAttribute("proveedores", proveedores);
        modelo.addAttribute("profesiones", profesiones);
        modelo.addAttribute("puntuacion", puntuacion);
        modelo.addAttribute("proveedoresFiltrados", proveedoresFiltrados);
        return "inicio.html";
    }

    @GetMapping("/filtrar")
    public String buscarPorProfesion(@RequestParam("profesion") String profesion, ModelMap modelo,
                                     RedirectAttributes redirectAttributes) {

        if (profesion!= null) {
           Profesion profesionn= profesionRepositorio.buscarProfesion(profesion);
            List<Proveedor> profesiones = proveedorRepositorio.findByProfesion(profesionn);

           // modelo.addAttribute("resultados",profesiones );
            redirectAttributes.addFlashAttribute("proveedoresFiltrados",profesiones );
            return "redirect:/inicio";
        }
        modelo.put("error", "No hay un proveedor para dicha profesion");
        return "redirect:/inicio";
    }

    @GetMapping("/filtrarPorNombre")
    public String filtrarXNombre(@RequestParam("nombre") String nombre, ModelMap modelo){


        return "redirect:/inicio";
    }

   /* @GetMapping("/buscar")
    public ResponseEntity<List<Proveedor>> buscarProveedores(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String profesion) {
        List<Proveedor> resultados = proveedorServicio.buscarProveedores(nombre, direccion, profesion);
        //return new ResponseEntity <>(resultados, HttpStatus.OK);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resultados.getBody());
    }*/


    @GetMapping("/listar")
    public String listarProveedores(ModelMap modelo) {
        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        modelo.addAttribute("proveedores", proveedores);

        return "lista_proveedores.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_ADMIN' , 'ROLE_PROVEEDOR')")
    @GetMapping("/iniciando")
    public String inicio(HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {

            return "redirect:/admin/index"; //esta vista aun no existe!!
        }
        return "redirect:/inicio";
    }
    
    /*@GetMapping("/registrarAdmin")
    public String registrar(){
        return "form_reg_admin.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam MultipartFile archivo, @RequestParam String nombre, @RequestParam String correo, @RequestParam String contrasenia,
            @RequestParam String contrasenia2, @RequestParam String direccion, ModelMap modelo){
        
        try {
            adminServ.registrarAdministrador(archivo, correo, nombre, correo, contrasenia, contrasenia2, direccion);
             modelo.put("exito", "Te has registrado como Cliente de manera correcta");
            return "index.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("correo", correo);
            modelo.put("direct", direccion);
            return "form_reg_admin.html";
            
        }
    }*/

}
