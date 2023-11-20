package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Comentario;
import com.egg.servicios.Entidades.Imagen;
import com.egg.servicios.enumeraciones.Rol;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.ClienteRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    //CREAR
    @Transactional
    public void crearCliente(MultipartFile archivo, String nombre, String correo,
            String contrasenia, String contrasenia2, String direccion,
            String barrio, String metodoPago, ModelMap modelo) throws MiException {

        validar(nombre, correo, contrasenia, contrasenia2, direccion, barrio);

        Cliente cliente = new Cliente();

        cliente.setActivo(true);
        cliente.setBarrio(barrio);
        cliente.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
        cliente.setCorreo(correo);
        cliente.setDireccion(direccion);
        cliente.setFechaAlta(new Date());
        cliente.setMetodoPago(metodoPago);
        cliente.setNombre(nombre);
        cliente.setRol(Rol.CLIENTE);

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
            String metodoPago, ArrayList<Comentario> comentarios) throws MiException {
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

}
