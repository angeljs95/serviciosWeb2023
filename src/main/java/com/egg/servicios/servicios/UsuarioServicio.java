package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Imagen;
import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.enumeraciones.Rol;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.UsuarioRepositorio;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearUsuario(MultipartFile archivo, String nombre, String correo,
                String contrasenia, String contrasenia2, String direccion) throws MiException {

        validar(nombre, correo, contrasenia, contrasenia2, direccion);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setDireccion(direccion);
        usuario.setCorreo(correo);
        usuario.setFechaAlta(new Date());
        usuario.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
        usuario.setRol(Rol.USUARIO);
        usuario.setActivo(true);

        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setImagen(imagen);
        usuarioRepositorio.save(usuario);

    }

    @Transactional(readOnly = true)
    public List listarUsuarios() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;

    }

    @Transactional
    public void modificarUsuario(MultipartFile archivo, String nombre, String idUsuario, String correo,
            String contrasenia, String contrasenia2, String direccion) throws MiException {

        validar(nombre, correo, contrasenia, contrasenia2, direccion);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setDireccion(direccion);
            usuario.setCorreo(correo);
            usuario.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
            String idImagen = null;
            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            usuario.setImagen(imagen);
            usuarioRepositorio.save(usuario);
        }
    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    public void eliminarUsuario(String idUsuario) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setActivo(Boolean.FALSE);
            usuarioRepositorio.save(usuario);
        }

    }

    private void validar(String nombre, String correo, String contrasenia, String contrasenia2, String direccion) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El usuario no puede estar en blanco");
        }
        if (correo.isEmpty() || correo == null) {
            throw new MiException("El Correo no puede estar en blanco");
        }
        if (contrasenia.isEmpty() || contrasenia == null ) { 
            throw new MiException("La contraseña no puede estar vacia");
            
        }else if (contrasenia.length() < 6){ 
            throw new MiException("La contraseña no puede ser menor de 6 caracteres");
        }
        if (!contrasenia.equals(contrasenia2)) {
            throw new MiException("La contraseña no coincide");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new MiException("Debe ingresar una direccion");
        }

    }

}
