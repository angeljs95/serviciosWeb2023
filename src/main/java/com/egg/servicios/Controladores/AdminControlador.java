package com.egg.servicios.Controladores;

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
    
    @GetMapping("/index")
    public String index(ModelMap modelo){
        return "panel.html";
    }
    
    @GetMapping("/listar")
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
    }
    
}
