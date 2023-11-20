package com.egg.servicios.Controladores;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Comentario;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
            /*@RequestParam String metodoPago*/ ModelMap modelo)  {
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

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Cliente> clientes = clienteServicio.listarClientes();

        modelo.addAttribute("cliente", clientes);

        return "cliente_list.html"; // nombre generico
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("cliente", clienteServicio.getOne(id));

        return "cliente_modificar.html";
    }

    @PostMapping("/modificar/{id}")
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
    }

    @GetMapping("/comentario/{id}") // comentario , palabra para cambiar segun veamos mas adelante
    public String Comentario(@PathVariable String id, ModelMap modelo) {

        modelo.put("cliente", clienteServicio.getOne(id));
        
        return "agregarComentario.html"; // a verificar
    }
    
    @PostMapping("/comentario/{id}")
    public String agregarComentarios(@PathVariable String id,String comentario ,ModelMap modelo){
        
       clienteServicio.agregarComentario(id, comentario);

        return "index.html"; // definir a donde vuelve
    }
    
}
