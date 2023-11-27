package com.egg.servicios.Controladores;

import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.servicios.ProveedorServicio;
import com.egg.servicios.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inicio")
public class InicioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private ProveedorServicio proveedorServicio;

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_ADMIN' , 'ROLE_PROVEEDOR')")
    @GetMapping("/index")
    public String inicio(HttpSession session, ModelMap modelo) {
        modelo.addAttribute("proveedores", proveedorServicio.listarProveedores());
        return "inicio.html";
    }

    @GetMapping("/perfil/{id}")
    public String perfil(@PathVariable String id, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(id));
        return "infoProv.html";
    }

}
