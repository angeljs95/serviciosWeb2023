package com.egg.servicios.Controladores;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.ClienteRepositorio;
import com.egg.servicios.repositorios.ProveedorRepositorio;
import com.egg.servicios.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @GetMapping("/index")
    public String index(ModelMap modelo) {

        modelo.put("usuarios", usuarioServicio.listarUsuarios());

        return "panel.html";
    }

    // Este metodo habilita y desabilita usuarios en un mismo bucle
    @GetMapping("/cambiarEstado/{id}")
    public String cambiarEstado(@PathVariable String idUsuario) {
        usuarioServicio.cambiarEstado(idUsuario);
        return "redirect:../index";
    }

    @GetMapping("/modificarAdmin/{id}")
    public String modificarAdmin(@PathVariable String id, ModelMap modelo) {
        modelo.put("admin", usuarioServicio.getOne(id));

        return "editar_admin.html";
    }

    @PostMapping("/modificarAdmin/{id}")
    public String modificarAdmin(@PathVariable String id, @RequestParam String nombre, @RequestParam String correo, @RequestParam String contrasenia,
                                 @RequestParam String direccion, @RequestParam MultipartFile archivo, ModelMap modelo) {
        try {
            usuarioServicio.modificar(archivo, nombre, id, correo, contrasenia, direccion);
            modelo.put("exito", "admin actualizado con exito!");
            return "redirect:../index";
        } catch (MiException ex) {
            modelo.put("error", "No se ha podido actualizar!");
            return "redirect:../index";
        }

    }

    @GetMapping("/modificar/{id}")
    public String modificarUsuarios(@PathVariable String id, ModelMap modelo) {
        Usuario usuario = usuarioServicio.getOne(id);

        if (usuario.getRol().toString().equals("PROVEEDOR")) {
            Proveedor proveedor = proveedorRepositorio.findById(usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con ID: " + usuario.getId()));
            modelo.put("usuario", proveedor);
            return "editar_proveedor.html";
        } else if (usuario.getRol().toString().equals("CLIENTE")) {
            Cliente cliente = clienteRepositorio.findById(usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + usuario.getId()));
            modelo.put("cliente", cliente);
            return "editar_cliente.html";
        }
        return null;
    }
}
/*
    @PostMapping("/modificar/{id}")
    public String modificado(){ }



}*/



    /*@GetMapping("/listar")
    public String listar(ModelMap modelo){

        modelo.put("usuarios", usuarioServicio.listarUsuarios());
        return "listar_usuario.html";
    }


    //proteger este metodo solo para el admin
    @GetMapping("/habilitar/{id}")
    public String habilitar(@PathVariable String id){
        usuarioServicio.habilitarUsuario(id);
        return "redirect:../listar";
    }

    @GetMapping("/deshabilitar/{id}")
    public String deshabilitar(@PathVariable String id){
        usuarioServicio.deshabilitarUsuario(id);
        return "redirect:../listar";
    }*/
