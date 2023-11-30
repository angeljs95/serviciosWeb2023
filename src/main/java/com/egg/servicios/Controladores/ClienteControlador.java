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
                           @RequestParam String contrasenia2, @RequestParam String direccion, @RequestParam String barrio,
            /*@RequestParam String metodoPago*/ ModelMap modelo) {
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
                clienteServicio.modificarCliente(archivo,nombre,correo,direccion,barrio,contrasenia,id);
                modelo.put("exito", "Se ha actualizado la informacion exitosamente");
                return "redirect:/admin/index";
            }

            clienteServicio.modificarCliente(archivo, nombre, idCliente, correo, contrasenia, direccion, barrio);
            modelo.put("exito", "Se ha actualizado la informacion exitosamente");
            return "infoCliente.html"; // definir a donde enviara nuevamente luego de modificar


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
                              @PathVariable String id, @RequestParam String idCliente, ModelMap modelo) throws MiException {

       // Cliente cliente = (Cliente) session.getAttribute("usuarioSession");
       Contrato contrato= contratoServicio.crearContrato(idCliente,id, descripcion);
        proveedorServicio.tareasEnCurso(contrato,id);

        modelo.put("exito", "El contrato se inicio exitosamente");
        return "redirect:../../inicio";
    }

}
