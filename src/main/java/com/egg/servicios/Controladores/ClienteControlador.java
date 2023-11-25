package com.egg.servicios.Controladores;

import ch.qos.logback.core.net.server.Client;
import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Comentario;
import com.egg.servicios.Entidades.ComentarioAux;
import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.servicios.ClienteServicio;
import com.egg.servicios.servicios.ComentarioServicio;
import com.egg.servicios.servicios.ProveedorServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private ProveedorServicio proveedorServicio;

    @Autowired
    private ComentarioServicio comentarioServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "form_reg_cliente.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam MultipartFile archivo, @RequestParam String nombre, @RequestParam String correo, @RequestParam String contrasenia,
                           @RequestParam String contrasenia2, @RequestParam String direccion, @RequestParam String barrio,
            /*@RequestParam String metodoPago*/ ModelMap modelo) {
        try {
            clienteServicio.crearCliente(archivo, nombre, correo, contrasenia, contrasenia2, direccion, barrio);
            modelo.put("exito", "Te has registrado como Cliente de manera correcta");
            return "inicio.html";
            
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("correo", correo);
            modelo.put("direct", direccion);
            modelo.put("barrio", barrio);
            return "form_reg_cliente.html";
        }

    }

    // muestra la informacion del cliente logueado
    @GetMapping("/perfil/{nombre}")
    public String perfil(ModelMap modelo, HttpSession session) {
       Cliente cliente = (Cliente) session.getAttribute("usuariosession");
        modelo.put("usuario",cliente);
        modelo.put("comentarios", cliente.getComentarios());
        return "infoProv.html";
    }

    @GetMapping("/modificarEstado")
    public String cambiarEstado(HttpSession session) {
       Cliente cliente = (Cliente) session.getAttribute("usuariosession");
        String idProveedor = cliente.getId();
        clienteServicio.cambiarEstado(idProveedor);
        return "redirect:/perfil/{nombre}";
    }

    @GetMapping("/modificar")
    public String modificar(ModelMap modelo, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("usuariosession");
        modelo.put("cliente", cliente);
        return "formulatio_modificar.html";
    }

    @PostMapping("/modificado")
    public String modificarCliente(HttpSession session, @RequestParam MultipartFile archivo, @RequestParam String nombre, @RequestParam String correo, @RequestParam String contrasenia,
                                   @RequestParam String contrasenia2, @RequestParam String direccion, @RequestParam Boolean activo, @RequestParam String barrio,
                                   @RequestParam String metodoPago, ModelMap modelo) {
        try {
            clienteServicio.modificarCliente(archivo, nombre, direccion, correo, contrasenia, contrasenia2, direccion, barrio, metodoPago);
            modelo.put("exito", "Se ha actualizado la informacion exitosamente");
            return "perfil_cliente"; // definir a donde enviara nuevamente luego de modificar
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            return "formulario_modificiar.html";
        }
    }
    @GetMapping("/comentario/{id}")
    public String aggComentario(@PathVariable String id, HttpSession session, ModelMap modelo) {
        Proveedor proveedor = proveedorServicio.getOne(id);
        modelo.put("usuario", proveedor);
        return "comentarioadd.html";
    }

    @PostMapping("/comentario/{id}")
    public String registrarComentario(@PathVariable String id, ModelMap modelo, HttpSession session, String comentario) {
        Proveedor proveedor = proveedorServicio.getOne(id);
        Cliente cliente = (Cliente) session.getAttribute("usuarioSession");
        ComentarioAux comentarioAux = new ComentarioAux();
        comentarioAux.setIdCliente(cliente.getId());
        comentarioAux.setIdProveedor(proveedor.getId());
        comentarioAux.setComentario(comentario);
        clienteServicio.agregarComentario(comentarioAux);
        return "inicio.html";

    }
}

   /*
    @PostMapping("/comentario/{id}")
    public String agregarComentarios(@PathVariable String id,String comentario ,ModelMap modelo){
        
       clienteServicio.agregarComentario(id, comentario);

        return "index.html"; // definir a donde vuelve
    }
*/



/*  @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("cliente", clienteServicio.getOne(id));

        return "cliente_modificar.html"; // vista de la informacion del cliente
    }

    /*@PostMapping("/modificar/{id}")
    public String modificarCliente(@PathVariable String id, @RequestParam MultipartFile archivo, @RequestParam String nombre, @RequestParam String correo, @RequestParam String contrasenia,
            @RequestParam String contrasenia2, @RequestParam String direccion, @RequestParam Boolean activo, @RequestParam String barrio,
            @RequestParam String metodoPago, ModelMap modelo) {
        try {
            List<Proveedor> proveedores = proveedorServicio.listarProveedores();
            modelo.addAttribute("proveedores", proveedores);

            clienteServicio.modificarCliente(archivo, nombre, direccion, correo, contrasenia, contrasenia2, direccion, barrio, metodoPago);

            return "index.html"; // definir a donde enviara nuevamente luego de modificar
        } catch (MiException ex) {
            List<Proveedor> proveedores = proveedorServicio.listarProveedores();
            modelo.addAttribute("proveedores", proveedores);// agrego estas dos lineas en caso de que vuelva a una lista

            modelo.put("error", ex.getMessage());

            return "cliente_modificar";
        }
    }*/
/*
    @GetMapping("/comentario/{id}") // comentario , palabra para cambiar segun veamos mas adelante
    public String Comentario(@PathVariable String id, ModelMap modelo) {

        modelo.put("cliente", clienteServicio.getOne(id));

        return "agregarComentario.html"; // a verificar
    }*/

/* @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Cliente> clientes = clienteServicio.listarClientes();

        modelo.addAttribute("cliente", clientes);

        return "cliente_list.html"; // nombre generico
    }*/