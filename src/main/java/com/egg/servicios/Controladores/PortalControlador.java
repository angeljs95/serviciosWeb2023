package com.egg.servicios.Controladores;


import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.servicios.ProveedorServicio;
import com.egg.servicios.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    ProveedorServicio proveedorServicio;

    @GetMapping("/")
    public String index(){
        return "index.html";
    }


    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "El usuario o la contraseña es incorrecta");
        }

        return "form_iniciar_sesion.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_ADMIN' , 'ROLE_PROVEEDOR')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "panel.html"; //esta vista aun no existe!!
        } 
        return "inicio.html";
    }

}

