package com.egg.servicios.Controladores;

import ch.qos.logback.core.net.server.Client;
import com.egg.servicios.Entidades.*;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.servicios.ClienteServicio;
import com.egg.servicios.servicios.ComentarioServicio;
import com.egg.servicios.servicios.ContratoServicio;
import com.egg.servicios.servicios.ProveedorServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private ContratoServicio contratoServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "form_reg_cliente.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam MultipartFile archivo, @RequestParam String nombre, @RequestParam String correo, @RequestParam String contrasenia,
                           @RequestParam String contrasenia2, @RequestParam String direccion, @RequestParam String barrio, ModelMap modelo) {
        try {
            clienteServicio.crearCliente(archivo, nombre, correo, contrasenia, contrasenia2, direccion, barrio);
            modelo.put("exito", "Te has registrado como Cliente de manera correcta");
            return "index.html";
            
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
    @GetMapping("/perfil/{id}")
    public String perfil(ModelMap modelo, HttpSession session) {
       Cliente cliente = (Cliente) session.getAttribute("usuariosession");
        modelo.put("usuario",cliente);
        modelo.put("comentarios", cliente.getComentarios());
        return "infoCliente.html";
    }

    @GetMapping("/modificarEstado")
    public String cambiarEstado(HttpSession session) {
       Cliente cliente = (Cliente) session.getAttribute("usuariosession");
        String idProveedor = cliente.getId();
        clienteServicio.cambiarEstado(idProveedor);
        return "redirect:/perfil/{nombre}";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(ModelMap modelo, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("usuariosession");
        modelo.put("cliente", cliente);
        return "editar_cliente.html";
    }

    @PostMapping("/modificado/{id}")
    public String modificarCliente(@PathVariable String id, HttpSession session, @RequestParam MultipartFile archivo, @RequestParam String nombre, @RequestParam String correo,
                                   @RequestParam String direccion, @RequestParam String barrio, @RequestParam String contrasenia,
                                    ModelMap modelo) {
       Usuario usuario = (Usuario) session.getAttribute("usuariosession");
       String idCliente= usuario.getId();
        try {
            if (usuario.getRol().toString().equals("ADMIN")){
                clienteServicio.modificarCliente(archivo, nombre, id, correo, contrasenia, direccion, barrio);
                Cliente cliente= clienteServicio.getOne(id);
                modelo.put("cliente", cliente);
                modelo.put("exito", "Se ha actualizado la informacion exitosamente");
                return "redirect:/admin/index";
            }
            Cliente cliente = (Cliente) session.getAttribute("usuariosession");
            cliente= clienteServicio.modificarCliente(archivo, nombre, idCliente, correo, contrasenia, direccion, barrio);
           session.setAttribute("usuariosession",cliente);
            modelo.put("exito", "Se ha actualizado la informacion exitosamente");
            modelo.addAttribute("usuario",cliente);
            return "redirect:/cliente/perfil/{id}"; // definir a donde enviara nuevamente luego de modificar


        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            return "editar_cliente.html";
        }
    }

    @GetMapping("/comentario/{id}")
    public String aggComentario(@PathVariable String id, HttpSession session, ModelMap modelo) {
        Proveedor proveedor = proveedorServicio.getOne(id);
        modelo.put("usuario", proveedor);
        return "comentarioadd.html";
    }

    @PostMapping("/comentado/{id}")
    public String guardarComentario(@PathVariable String id, ModelMap modelo, HttpSession session, String comentario) {
        Proveedor proveedor = proveedorServicio.getOne(id);
        Cliente cliente = (Cliente) session.getAttribute("usuarioSession");
        ComentarioAux comentarioAux = new ComentarioAux();
        comentarioAux.setIdCliente(cliente.getId());
        comentarioAux.setIdProveedor(proveedor.getId());
        comentarioAux.setComentario(comentario);
        clienteServicio.agregarComentario(comentarioAux);
        return "inicio.html";

    }


    @GetMapping("/contratar/{id}")
    public  String contratar(@PathVariable String id, HttpSession session, ModelMap modelo){
        //llega el id del proveedor a traves de un path variable
      //  Cliente cliente = (Cliente) session.getAttribute("usuarioSession");
        Proveedor proveedor= proveedorServicio.getOne(id);
        //modelo.addAttribute("cliente", cliente);
       modelo.addAttribute("proveedor", proveedor);
        return "contrato.html";
    }

    @PostMapping("/contratado/{id}")
    public  String contratado(@RequestParam String descripcion,HttpSession session,
                              @PathVariable String id, @RequestParam String idCliente, ModelMap modelo){

       Contrato contrato= contratoServicio.crearContrato(idCliente,id, descripcion);
        proveedorServicio.tareasEnCurso(contrato);

        modelo.put("exito", "El contrato se inicio exitosamente");
        return "redirect:../../inicio";
    }

}

   /*
    @PostMapping("/comentario/{id}")
    public String agregarComentarios(@PathVariable String id, String comentario, ModelMap modelo) {

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

