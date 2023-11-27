package com.egg.servicios.Controladores;

import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.servicios.AdminServicio;
import com.egg.servicios.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private AdminServicio adminServicio;
    
    @GetMapping("/modificar/{id}")
    public String modificarAdmin(@PathVariable String id, ModelMap modelo){
        modelo.put("admin", adminServicio.getOne(id));
        
        return "editar_admin.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificarAdmin(@PathVariable String id, @RequestParam String nombre, @RequestParam String correo, @RequestParam String contrasenia,
             @RequestParam String direccion, @RequestParam MultipartFile archivo, ModelMap modelo){
        try {
            adminServicio.modificarAdmin(archivo, nombre, id, correo, contrasenia, direccion);
            modelo.put("exito", "admin actualizado con exito!");
            return "redirect:../index";
        } catch (MiException ex) {
            modelo.put("error", "No se ha podido actualizar!");
            return "redirect:../index";
        }
    
    }

    @GetMapping("/index")
    public String index(ModelMap modelo) {
        modelo.put("usuarios", usuarioServicio.listarUsuarios());
        return "panel.html";
    }

    //proteger este metodo solo para el admin
    @GetMapping("/habilitar/{id}")
    public String habilitar(@PathVariable String id) {
        usuarioServicio.habilitarUsuario(id);
        return "redirect:../index";
    }

    @GetMapping("/deshabilitar/{id}")
    public String deshabilitar(@PathVariable String id) {
        usuarioServicio.deshabilitarUsuario(id);
        return "redirect:../index";
    }
}
