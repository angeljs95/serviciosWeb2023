package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Comentario;
import com.egg.servicios.Entidades.Imagen;
import com.egg.servicios.enumeraciones.Rol;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.ClienteRepositorio;
import com.egg.servicios.repositorios.ComentarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;
    
    @Autowired
    private ComentarioRepositorio comentarioRepositorio;

    
    @Transactional
    public void crearCliente(MultipartFile archivo, String nombre, String correo,
            String contrasenia, String contrasenia2, String direccion,
            String barrio, String metodoPago) throws MiException {

        validar(nombre, correo, contrasenia, contrasenia2, direccion, barrio);

        Cliente cliente = new Cliente();
        
        //vamos a setear todos los parametros de un usuario!!!!!!!!!
        
        cliente.setNombre(nombre);
        cliente.setCorreo(correo);
        cliente.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
        cliente.setDireccion(direccion);
        cliente.setActivo(true);
        cliente.setFechaAlta(new Date());
        cliente.setRol(Rol.CLIENTE);
        
        //seteamos los atributos particulares de un Cliente!!!!
        cliente.setBarrio(barrio);
        cliente.setMetodoPago(metodoPago);
        cliente.setComentarios(new ArrayList<>());
        cliente.setProveedores(new ArrayList<>());
        
        //guardamos la imagen de perfil!!!
        Imagen imagen = imagenServicio.guardar(archivo);
        cliente.setImagen(imagen);
        

        clienteRepositorio.save(cliente);

    }

    //LEER
    @Transactional(readOnly = true)
    public List listarClientes() {
        List<Cliente> clientes = new ArrayList();
        clientes = clienteRepositorio.findAll();
        return clientes;
    }

    // MODIFICAR
    @Transactional
    public void modificarCliente(MultipartFile archivo, String nombre, String idCliente, String correo,
            String contrasenia, String contrasenia2, String direccion, String barrio,
            String metodoPago) throws MiException {
        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);

        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            cliente.setNombre(nombre);
            cliente.setCorreo(correo);
            cliente.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
            cliente.setDireccion(direccion);
            cliente.setMetodoPago(metodoPago);
            cliente.setBarrio(barrio);

            String idImagen = null;
            if (cliente.getImagen() != null) {
                idImagen = cliente.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            cliente.setImagen(imagen);
            clienteRepositorio.save(cliente);
        }
    }

    // ELIMINAR
    public void eliminarCliente(String idCliente) {

        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);

        Cliente cliente = respuesta.get();
        cliente.setActivo(Boolean.FALSE);

        clienteRepositorio.save(cliente);
    }

    private void validar(String nombre, String correo,
            String contrasenia, String contrasenia2,
            String direccion, String barrio) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El usuario no puede estar en blanco");
        }
        if (correo.isEmpty() || correo == null) {
            throw new MiException("El Correo no puede estar en blanco");
        }
        if (contrasenia.isEmpty() || contrasenia == null) {
            throw new MiException("La contraseña no puede estar vacia");

        } else if (contrasenia.length() < 6) {
            throw new MiException("La contraseña no puede ser menor de 6 caracteres");
        }
        if (!contrasenia.equals(contrasenia2)) {
            throw new MiException("La contraseña no coincide");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new MiException("Debe ingresar una direccion");
        }

        if (barrio.isEmpty() || barrio == null) {
            throw new MiException("Debe ingresar un Barrio");
        }

    }
    @Transactional(readOnly = true)
    public Cliente getOne(String id){
        return clienteRepositorio.getOne(id);
    }
    
    /*@Transactional
    public Cliente agregarComentario(String idCliente, String comentario){

        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);
        Cliente cliente = respuesta.get();
        
        if(respuesta.isPresent()){
            
            Comentario com = comentarioServicio.crearComentario(comentario);
            List<Comentario> comentarios = new ArrayList();
            comentarios.add(com);
            cliente.setComentarios((ArrayList<Comentario>) comentarios);
            clienteRepositorio.save(cliente);
            
        }
        
        return cliente;
        
    }*/
    
    @Transactional
    public void agregarComentario(String idCliente, String comentario){

        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);

        if(respuesta.isPresent()){
            
            Cliente cliente = respuesta.get();
            Comentario com = new Comentario();
            com.setComentario(comentario);
            ArrayList<Comentario> comentarios = new ArrayList();
            comentarios.add(com);
            cliente.setComentarios(comentarios);
            comentarioRepositorio.save(com);
            clienteRepositorio.save(cliente);
   
        }
        
    }
               /* Comentario com = comentarioServicio.crearComentario(comentario);
            List<Comentario> comentarios = new ArrayList();
            comentarios.add(com);
            cliente.setComentarios((ArrayList<Comentario>) comentarios);
            clienteRepositorio.save(cliente);*/


/*@Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Cliente cliente = clienteRepositorio.buscarPorEmail(correo);

        if (cliente != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + cliente.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session;
            session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", cliente);


            return new User(cliente.getCorreo(), cliente.getContrasenia(), permisos);
        } else {
            return null;
        }
    }*/
}
