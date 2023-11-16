package com.egg.servicios.Controladores;

import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.servicios.ProveedorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/proveedor")
public class ProveedorControlador {

    @Autowired
    private ProveedorServicio proveedorServicio;

    @GetMapping("/registrar")
    public String registrar(){
        return "form_proveedor.html";

    }
    @PostMapping("/registro")
    public String registro(){

        return null;
    }

    @GetMapping("listar")
    public String listarProveedores(){
    List<Proveedor> proveedores= proveedorServicio.listarProveedores();
        return "lista_proveedores.html";
    }

    @GetMapping("/perfil")
    public String modificar(){
 return null;
    }


}