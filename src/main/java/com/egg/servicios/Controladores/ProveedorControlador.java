package com.egg.servicios.Controladores;

import com.egg.servicios.Entidades.*;
import com.egg.servicios.enumeraciones.Profesiones;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.ProveedorRepositorio;
import com.egg.servicios.servicios.ClienteServicio;
import com.egg.servicios.servicios.ContratoServicio;
import com.egg.servicios.servicios.ProfesionServicio;
import com.egg.servicios.servicios.ProveedorServicio;
import org.apache.coyote.Response;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/proveedor")
public class ProveedorControlador {

    @Autowired
    private ProveedorServicio proveedorServicio;
    @Autowired
    private ContratoServicio contratoServicio;
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    ImagenControlador imagenControlador;
    @Autowired
    ProveedorRepositorio proveedorRepositorio;


    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
        modelo.addAttribute("profesiones", profesiones);
        return "form_reg_proveedor.html";
    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String nombre, @RequestParam String correo,
                           @RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam String direccion,
                           @RequestParam String profesion, /*@RequestParam Integer cbu*/ @RequestParam Double costoXHora,
            /*@RequestParam String matricula*/ @RequestParam String descripcion, ModelMap modelo) throws MiException {
        try {
            proveedorServicio.crearProveedor(archivo, nombre, correo, contrasenia,
                    contrasenia2, direccion, profesion, costoXHora, descripcion);
            modelo.put("exito", "El proveedor ha sido registrado exitosamente");
            return "index.html";
            //Por ahora retorna al index, luego ver a donde retorna
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("correo", correo);
            modelo.put("direct", direccion);
            modelo.put("descrip", descripcion);
            List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
            modelo.addAttribute("profesiones", profesiones);
            return "form_reg_proveedor.html";

        }
    }

    @GetMapping("/perfil/{id}")
    public String perfil(ModelMap modelo, HttpSession session, @PathVariable String id) throws MiException {
        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
        List<Contrato> contratos = contratoServicio.obtenerContratosActivos(proveedor);
        List <Contrato> cProveedor= proveedorServicio.listarTrabajosEnCurso(proveedor.getId());
        List <Contrato>  contratosFinish= proveedorServicio.listarTrabajosFinalizados(proveedor.getId());
        modelo.addAttribute("imagenesInfo", proveedorServicio.retornarAlbum(id));
        modelo.put("proveedor", proveedor);
        modelo.put("comentarios", proveedor.getComentarios());
        modelo.addAttribute("contratosActivos",cProveedor);
        modelo.addAttribute("contratos", contratos);
        modelo.addAttribute("contratosFinalizados" , contratosFinish );
        return "infoProv.html";

    }

    @GetMapping("/modificarEstado")
    public String cambiarEstado(HttpSession session) {
        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
        String idProveedor = proveedor.getId();
        proveedorServicio.cambiarEstado(idProveedor);
        return "redirect:/perfil/{nombre}";
    }


    @GetMapping("/modificar/{id}")
    public String modificarPerfil(ModelMap modelo, HttpSession session, @PathVariable String id) {
        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
        List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
        modelo.addAttribute("profesiones", profesiones);
        modelo.put("proveedor", proveedor);


        return "editar_proveedor.html";
    }

    @PostMapping("/modificado/{id}")
    public String modificarPerfill(MultipartFile archivo, @PathVariable String id, HttpSession session,
                                   @RequestParam String nombre, @RequestParam String correo,
                                   @RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam String direccion,
                                   @RequestParam String profesion, @RequestParam Double costoXHora, ModelMap modelo) throws MiException {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        String idProveedor = usuario.getId();
        try {
            if (usuario.getRol().toString().equals("ADMIN")) {
                proveedorServicio.modificarProveedor(archivo, nombre, correo, contrasenia,
                        contrasenia2, direccion, profesion, costoXHora, id);
                modelo.put("exito", "Se ha actualizado la informacion exitosamente");
                return "redirect:/admin/index";
            }
            Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
            proveedor = proveedorServicio.modificarProveedor(archivo, nombre, correo, contrasenia,
                    contrasenia2, direccion, profesion, costoXHora, idProveedor);
            session.setAttribute("usuariosession", proveedor);
            modelo.put("exito", "El usuario " + usuario.getNombre() + " se ha actualizado correctamente");
            modelo.addAttribute("proveedor", proveedor);
            return "redirect:/proveedor/perfil/{id}";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", correo);
            modelo.put("direccion", direccion);
            modelo.put("costoXHora", costoXHora);
            List<Proveedor> profesiones = proveedorServicio.listarProfesiones();
            modelo.addAttribute("profesiones", profesiones);
            return "editar_proveedor.html";

        }
    }


// metodo de comunicacion con el cliente


    @GetMapping("/aceptar/{id}")
    public String aceptar(@PathVariable String id, HttpSession session) {

        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");

        proveedorServicio.aceptarTrabajo(clienteServicio.getOne(id), proveedor);


        return "redirect:../perfil/" + proveedor.getId().toString();

    }

   /* @GetMapping("/declinar/{id}")
    public String declinar(@PathVariable String id, HttpSession session) {
        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
        proveedorServicio.declinarTrabajo(clienteServicio.getOne(id), proveedor);
        return "redirect:../perfil/" + proveedor.getId().toString();
    }*/

    @GetMapping("/declinar/{id}")
    public String declinar(@PathVariable String id, HttpSession session) {
        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
        Contrato contrato = contratoServicio.obtenerContratoactivo(proveedor, clienteServicio.getOne(id));
        proveedorServicio.declinarTrabajo(contrato);
        return "redirect:../perfil/" + proveedor.getId().toString();
    }

    @GetMapping("/terminado/{id}")
    public String terminado(@PathVariable String id, HttpSession session) {
        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
        Contrato contrato = contratoServicio.obtenerContratoactivo(proveedor, clienteServicio.getOne(id));
            proveedorServicio.terminadoTrabajo(contrato);
        return "redirect:../perfil/" + proveedor.getId().toString();
    }

    @PostMapping("/añadirImagenAlbum")
    public String agregarImagenAlbum(MultipartFile archivo, ModelMap modelo,
                                     HttpSession session) throws MiException {
        Proveedor proveedor= (Proveedor) session.getAttribute("usuariosession");
        proveedorServicio.crearAlbum(archivo, proveedor.getId());
        modelo.put("exito", "se ha añadido la imagen");
        return "redirect:/proveedor/perfil/"  + proveedor.getId().toString();
    }

    @GetMapping("/comentario/{id}")
    public String aggComentario(@PathVariable String id, ModelMap modelo) {
        Cliente cliente = clienteServicio.getOne(id);
        modelo.put("usuario", cliente);
        return "comentar.html";
    }

    @PostMapping("/comentado/{id}")
    public String guardarComentario(@PathVariable String id, ModelMap modelo, HttpSession session,
                                    @RequestParam String comentario) {
        Cliente cliente = clienteServicio.getOne(id);
        Proveedor proveedor = (Proveedor) session.getAttribute("usuariosession");
        ComentarioAux comentarioAux = new ComentarioAux();
        comentarioAux.setIdCliente(cliente.getId());
        comentarioAux.setIdProveedor(proveedor.getId());
        comentarioAux.setComentario(comentario);

        proveedorServicio.comentarAlCliente(comentarioAux);
        modelo.put("exito", "su comentario ha sido agregado a "+ cliente.getNombre()+".");
        return "redirect:../perfil/{id}";

    }


}

//Quise devolver la lista asi
      /*
        int i;
       List<byte[]> imagenes= new ArrayList<>();
        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.IMAGE_JPEG);

        for (i=0; i<proveedor.getImagenes().size(); i++) {
           // Imagen img = proveedor.getImagenes().get(i);
           // imagenes.add(img.getContenido());
            imagenes.add(proveedor.getImagenes().get(i).getContenido());
            System.out.println("se agrego");
        }*/
//  modelo.addAttribute("listaImagen",imagenControlador.imagenes(id));
//modelo.addAttribute( "listaImagen", imagination);

