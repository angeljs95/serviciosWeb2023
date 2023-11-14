package com.egg.servicios.Controladores;


//import com.egg.servicios.Entidades.Usuario;

import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.servicios.UsuarioServicio;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(){
        return "index.html";
    }

    @GetMapping("/registrar")
    public String registrar(){
        return "FormReg.html";
    }
    
    //agregar getMapping /registrarProvedor
    //agregar getMapping /registrarCliente

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String correo, @RequestParam String direccion,
                           @RequestParam String contrasenia, String contrasenia2, MultipartFile archivo,
                           ModelMap modelo) throws MiException {
        try {
            usuarioServicio.crearUsuario(archivo, nombre, correo, contrasenia, contrasenia2, direccion);
            modelo.put("exito", "El usuario se ha registrado correctamente");
            return "index.html";
        } catch (MiException ex){
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("correo", correo);
            modelo.put("direc", direccion);
            return "FormReg.html";
        }
    }

    @GetMapping("/listar")
    public String listarUsuarios(ModelMap modelo){
        List<Usuario> usuarios= usuarioServicio.listarUsuarios();
        modelo.put("usuarios", usuarios);
        return "usuario_listar.html";

    }


}
