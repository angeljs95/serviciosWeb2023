package com.egg.servicios.Controladores;


import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.servicios.ProveedorServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private ProveedorServicio proveedorServicio;
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

    @GetMapping("/listar")
    public String listarProveedores(ModelMap modelo) {
        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        modelo.addAttribute("proveedores", proveedores);
        return "lista_proveedores.html";
    }

    //atributo del admin (PORTALADMIN)

    /*@GetMapping("/listar")
    public String listarUsuarios(ModelMap modelo){
        List<Usuario> usuarios= usuarioServicio.listarUsuarios();
        modelo.put("usuarios", usuarios);
        return "usuario_listar.html";
    }*/

   
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_ADMIN' , 'ROLE_PROVEEDOR')")
    @GetMapping("/iniciando")
    public String inicio(HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/panel.html"; //esta vista aun no existe!!
        }
        return "redirect:/inicio/index";
    }

}
