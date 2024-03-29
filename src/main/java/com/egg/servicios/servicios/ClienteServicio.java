package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.*;
import com.egg.servicios.enumeraciones.Rol;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.ClienteRepositorio;
import com.egg.servicios.repositorios.ComentarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;
    
    @Autowired
    private ComentarioRepositorio comentarioRepositorio;

    @Autowired
    private ComentarioServicio comentarioServicio;
    
    @Autowired
    private ContratoServicio contratoServicio;

    @Autowired
    private ProveedorServicio proveedorServicio;


    
    @Transactional
    public void crearCliente(MultipartFile archivo, String nombre, String correo,
            String contrasenia, String contrasenia2, String direccion,
            String barrio /*String metodoPago*/) throws MiException {
        

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
        cliente.setMetodoPago(null);
       // cliente.setComentarioss(new ArrayList<>());
        //cliente.setProveedores(new ArrayList<>());
        
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
//
    // MODIFICAR
    @Transactional
    public Cliente modificarCliente(MultipartFile archivo, String nombre, String idCliente, String correo,
                                 String contrasenia, String direccion, String barrio) throws MiException {
        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);

        if (respuesta.isPresent()) {
            
            System.out.println("Estoy deontro de modificar cliente");
            Cliente cliente = respuesta.get();
            cliente.setNombre(nombre);
            cliente.setCorreo(correo);
            cliente.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
            cliente.setDireccion(direccion);
            cliente.setBarrio(barrio);
            if(archivo.isEmpty()) {
                Imagen imagen = cliente.getImagen();
                cliente.setImagen(imagen);
                clienteRepositorio.save(cliente);
            } else {
                String idImagen = cliente.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                cliente.setImagen(imagen);
                clienteRepositorio.save(cliente);
            }
            return cliente;
            }return null;
        }

    // ELIMINAR
    @Transactional
    public void cambiarEstado(String id) {
        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();

            if (cliente.getActivo().equals(true)) {
                cliente.setActivo(false);
            } else if (cliente.getActivo().equals(false)) {
                cliente.setActivo(true);
            }
        }
    }


    public void habilitarCliente(String idCliente) {
        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);

        if (respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
            cliente.setActivo(Boolean.TRUE);
            clienteRepositorio.save(cliente);
        }

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
        
        if (contrasenia.isEmpty() || contrasenia2.isEmpty() || contrasenia == null || contrasenia2 == null) {
            throw new MiException("La contraseña no puede estar vacia");
        } else if (!contrasenia.equals(contrasenia2)){
            throw new MiException("Las contraseñas no coinciden");
        } else if (contrasenia.length() < 6){
            throw new MiException("La contraseña no puede ser menor de 6 caracteres");
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



    public void agregarComentario(ComentarioAux comentarioAux){
        Optional<Proveedor> respuesta= Optional.ofNullable(proveedorServicio.getOne(comentarioAux.getIdProveedor()));
        if(respuesta.isPresent()) {
            Proveedor proveedor =respuesta.get();

            if(proveedor.getContratoFinalizadoP().contains(comentarioAux.getIdCliente())){
            Comentario comentario = comentarioServicio.crearComentario(comentarioAux);
            proveedor.getComentarios().add(comentario);
            proveedorServicio.guardar(proveedor);
            }
        }
    }

    @Transactional
    public void guardar(Cliente cliente ){
        clienteRepositorio.save(cliente);

    }
    
    @Transactional
    public void agregarContrato(Contrato contrato){

        Cliente cliente= getOne(contrato.getCliente().getId());
        cliente.getContratoEnCursoC().add(contrato);
        if(!cliente.getProveedores().contains(contrato.getProveedor())){
            cliente.getProveedores().add(contrato.getProveedor());
            clienteRepositorio.save(cliente);
        } else {
            clienteRepositorio.save(cliente);
        }

    }

    @Transactional
    public void finalizarContrato(Contrato contrato){
  Cliente cliente= getOne(contrato.getCliente().getId());
  if (cliente != null){
        cliente.getContratoEnCursoC().remove(contrato);
        cliente.getContratoFinalizadoC().add(contrato);
        clienteRepositorio.save(cliente);}
    }

    @Transactional
    public void declinarTrabajoCliente(Contrato contrato){
        Cliente cliente= getOne(contrato.getCliente().getId());
        if (cliente != null){
            cliente.getContratoEnCursoC().remove(contrato);
            clienteRepositorio.save(cliente);}
    }


    






    /*@Transactional
    public void agregarComentario(String idCliente, String comentario){

        Optional<Cliente> respuesta= clienteRepositorio.findById(idCliente);
        if(respuesta.isPresent()) {
            Cliente cliente = respuesta.get();
           Comentario comentario1= comentarioServicio.crearComentario(comentario);
            ArrayList<Comentario> coment= new ArrayList<>();
            coment.add(comentario1);
            cliente.setComentarios(coment);
            clienteRepositorio.save(cliente);
        }
        
    }*/
}
