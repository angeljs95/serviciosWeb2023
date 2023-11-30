
package com.egg.servicios.Controladores;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Proveedor;
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

import java.util.List;

//@Controller
//@RequestMapping("/inicio")
public class InicioControlador {
/*
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ProveedorServicio proveedorServicio;

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_ADMIN' , 'ROLE_PROVEEDOR')")
    @GetMapping("/index")
    public String inicio(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "panel.html"; //esta vista aun no existe!!
        }
        List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
       // List<Proveedor> estadoActivo = proveedorServicio.proveedoresActivos();
       // List<Proveedor> estadoInactivo = proveedorServicio.proveedoresInactivos();

        modelo.addAttribute("profesion", profesiones);
        //modelo.addAttribute("estadoA", estadoInactivo);
        //modelo.addAttribute("estadoI", estadoActivo);
        return "inicio.html";
    }

    @GetMapping("/perfil/{nombre}")
    public String perfil(ModelMap modelo, HttpSession session) {
        Proveedor cliente = (Proveedor) session.getAttribute("usuariosession");
        modelo.put("usuario", cliente);
        modelo.put("comentarios", cliente.getComentarios());
        return "infoCliente.html";
    }

    /*@GetMapping("/perfil/{id}")
    public String perfil(@PathVariable String id, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(id));
        return "infoCliente.html";
    }*/
}
