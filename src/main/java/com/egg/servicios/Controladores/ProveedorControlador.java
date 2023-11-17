package com.egg.servicios.Controladores;

import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.enumeraciones.Profesiones;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.servicios.ProveedorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/proveedor")
public class ProveedorControlador {

    @Autowired
    private ProveedorServicio proveedorServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo){
        List<Proveedor> profesiones= proveedorServicio.listarProfesiones();
        modelo.addAttribute("profesion", profesiones);
        return "form_proveedor.html";

    }
    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String nombre, @RequestParam String correo,
                           @RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam  String direccion,
                           @RequestParam Profesiones profesion, @RequestParam Integer cbu, @RequestParam Double costoXHora,
                           @RequestParam String matricula, ModelMap modelo) throws MiException {
        try {
            proveedorServicio.crearProveedor(archivo, nombre, correo, contrasenia,
                    contrasenia2, direccion, profesion, cbu, costoXHora, matricula);

            modelo.put("exito", "El proveedor ha sido registrado exitosamente");
            return "index.html";
            //Por ahora retorna al index, luego ver a donde retorna
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", correo);
            List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
            modelo.addAttribute("profesion", profesiones);
            return "form_proveedor.html";

        }
    }

    @GetMapping("/listar")
    public String listarProveedores (ModelMap modelo){
    List<Proveedor> proveedores= proveedorServicio.listarProveedores();
    modelo.addAttribute("proveedores", proveedores);
        return "lista_proveedores.html";
    }

    /*
      @GetMapping("/perfil")
      public String perfil(ModelMap modelo,HttpSession session){
           Proveedor proveedor= (Proveedor) session.getAttribute("usuariosession");
           modelo.put("proveedor", proveedor);
           return "perfil_proveedor.html";}*/

     /*
      @GetMapping("/perfil/modificar")
      public String modificarPerfil(ModelMap modelo,HttpSession session){
           Proveedor proveedor= (Proveedor) session.getAttribute("usuariosession");
           modelo.put("proveedor", proveedor);
           return "modificar_proveedor.html";}*/

     /*
      @PostMapping("/perfil/modificar/{id}")
      public String modificarPerfil(ModelMap modelo,HttpSession session){
           Proveedor proveedor= (Proveedor) session.getAttribute("usuariosession");
           modelo.put("proveedor", proveedor);
           return "modificar_proveedor.html";}*/

    //el metodo post modificar enta mas abajo






    @GetMapping("/perfil")
    public String obtenerPerfil(ModelMap modelo, String idProveedor){
        Proveedor proveedor= proveedorServicio.getOne(idProveedor);
        modelo.addAttribute("proveedor",proveedor);
        // proveedor.getActivo()
        return "perfil_proveedor.html";

    }

    @GetMapping("perfil/actualizar")
    public String actualizarProveedor(){
        List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
        return "proveedor_modificar.html";
    }

    @PostMapping ("/perfil/{id}")
        public String actualizarPerfil( MultipartFile archivo, @PathVariable String idProveedor,
                                       @RequestParam String nombre, @RequestParam String correo,
                                       @RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam  String direccion,
                                       @RequestParam Profesiones profesion, @RequestParam Integer cbu,
                                       @RequestParam Double costoXHora,
                                       @RequestParam String matricula, ModelMap modelo) throws MiException {
//el getOne es temporal con el httpsession no iria
        Proveedor proveedor = proveedorServicio.getOne(idProveedor);
        try {
            proveedorServicio.modificarProveedor(archivo, nombre, correo, contrasenia,
                    contrasenia2, direccion, profesion, cbu, costoXHora, matricula, idProveedor);
            modelo.put("exito", "El usuario " + proveedor.getNombre() + " se ha actualizado correctamente");
            return "perfil_proveedor.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", correo);
            modelo.put("direccion", direccion);
            modelo.put("cbu", cbu);
            modelo.put("costoXHora", costoXHora);
            modelo.put("matricula", matricula);
            List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
            return "proveedor_modificar.html";
        }
    }
// metodo de comunicacion con el cliente
        @GetMapping("perfil/contacto")
                public String contactar (){
            //investigar comoc comunicarse entre cliente y proveedor
    return null;
        }
  }


