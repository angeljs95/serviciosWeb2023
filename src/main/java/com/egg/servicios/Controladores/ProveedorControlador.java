package com.egg.servicios.Controladores;

import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.enumeraciones.Profesiones;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.servicios.ProveedorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/proveedor")
public class ProveedorControlador {

    @Autowired
    private ProveedorServicio proveedorServicio;


    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
        modelo.addAttribute("profesiones", profesiones);
        return "form_reg_proveedor.html";
    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String nombre, @RequestParam String correo,
                           @RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam String direccion,
                           @RequestParam Profesiones profesion, /*@RequestParam Integer cbu*/ @RequestParam Double costoXHora,
            /*@RequestParam String matricula*/ @RequestParam String descripcion, ModelMap modelo) throws MiException {
        try {
            proveedorServicio.crearProveedor(archivo, nombre, correo, contrasenia,
                    contrasenia2, direccion, profesion, costoXHora, descripcion);
            modelo.put("exito", "El proveedor ha sido registrado exitosamente");
            return "index.html";
            //Por ahora retorna al index, luego ver a donde retorna
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("correo", correo);
            modelo.put("direct", direccion);
            modelo.put("descrip", descripcion);
            List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
            modelo.addAttribute("profesion", profesiones);
            return "form_reg_proveedor.html";

        }
    }

    @GetMapping("/perfil/{nombre}")
    public String perfil(ModelMap modelo, HttpSession session) {
        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
        modelo.put("usuario",proveedor);
        modelo.put("comentarios", proveedor.getComentarios());
        return "infoProv.html";
    }

    @GetMapping("/modificarEstado")
    public String cambiarEstado(HttpSession session) {
        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
        String idProveedor = proveedor.getId();
        proveedorServicio.cambiarEstado(idProveedor);
        return "redirect:/perfil/{nombre}";
    }


    @GetMapping("/modificar/{nombre}")
    public String modificarPerfil(ModelMap modelo, HttpSession session) {
        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
        List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
        modelo.put("proveedor", proveedor);
        modelo.addAttribute("profesion", profesiones);

        return "editar_proveedor.html";
    }

    @PostMapping("/modificado/{id}")
    public String modificarPerfill(MultipartFile archivo,@PathVariable String id, HttpSession session,
                                   @RequestParam String nombre, @RequestParam String correo,
                                   @RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam String direccion,
                                   @RequestParam Profesiones profesion, @RequestParam Double costoXHora, ModelMap modelo) throws MiException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        String idProveedor = usuario.getId();
        try {
            if (usuario.getRol().toString().equals("ADMIN")){
                proveedorServicio.modificarProveedor(archivo, nombre, correo, contrasenia,
                        contrasenia2, direccion, profesion, costoXHora, id);
                modelo.put("exito", "Se ha actualizado la informacion exitosamente");
                return "redirect:/admin/index";}

            proveedorServicio.modificarProveedor(archivo, nombre, correo, contrasenia,
                    contrasenia2, direccion, profesion, costoXHora, idProveedor);
            modelo.put("exito", "El usuario " + usuario.getNombre() + " se ha actualizado correctamente");
            return "redirect:/proveedor/perfil/{nombre}";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", correo);
            modelo.put("direccion", direccion);
            modelo.put("costoXHora", costoXHora);
            List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
            modelo.addAttribute("profesion", profesiones);
            return "editar_proveedor.html";
        }
    }




























 /*
      @PostMapping("/perfil/modificar/{id}")
      public String modificarPerfil(ModelMap modelo,HttpSession session){
           Proveedor proveedor= (Proveedor) session.getAttribute("usuariosession");
           modelo.put("proveedor", proveedor);
           return "modificar_proveedor.html";}*/
    //el metodo post modificar enta mas abajo
    
   /* @GetMapping("/perfil")
    public String obtenerPerfil(ModelMap modelo, String idProveedor){
        Proveedor proveedor= proveedorServicio.getOne(idProveedor);
        modelo.addAttribute("proveedor",proveedor);
        return "perfil_proveedor.html";
    }*/

   /* @GetMapping("/perfil/actualizar")
    public String actualizarProveedor() {
        List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
        // proveedor.getActivo()
        return "proveedor_modificar.html";
    }



*/



































    @PostMapping("/perfil/{id}")
    public String actualizarPerfil(MultipartFile archivo, @PathVariable String idProveedor,
            @RequestParam String nombre, @RequestParam String correo,
            @RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam String direccion,
            @RequestParam Profesiones profesion, /*@RequestParam Integer cbu,*/
            @RequestParam Double costoXHora,
            /*@RequestParam String matricula,*/ ModelMap modelo) throws MiException {
//el getOne es temporal con el httpsession no iria
        Proveedor proveedor = proveedorServicio.getOne(idProveedor);
        try {
            proveedorServicio.modificarProveedor(archivo, nombre, correo, contrasenia,
                    contrasenia2, direccion, profesion, /*cbu,*/ costoXHora, /* matricula,*/ idProveedor);
            modelo.put("exito", "El usuario " + proveedor.getNombre() + " se ha actualizado correctamente");
            return "perfil_proveedor.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", correo);
            modelo.put("direccion", direccion);
//            modelo.put("cbu", cbu);
            modelo.put("costoXHora", costoXHora);
//            modelo.put("matricula", matricula);
            List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
            return "proveedor_modificar.html";
        }
    }
// metodo de comunicacion con el cliente

    @GetMapping("/perfil/contacto")
    public String contactar() {
        //investigar comoc comunicarse entre cliente y proveedor
        return null;
    }
}
