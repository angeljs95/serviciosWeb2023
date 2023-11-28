package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Imagen;
import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.enumeraciones.Rol;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.ClienteRepositorio;
import com.egg.servicios.repositorios.ProveedorRepositorio;
import com.egg.servicios.repositorios.UsuarioRepositorio;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;


    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List listarUsuarios() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }


    @Transactional
    public void cambiarEstado(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getActivo().equals(true)) {
                usuario.setActivo(false);
            } else if (usuario.getActivo().equals(false)) {
                usuario.setActivo(true);
            }
        }
    }

    @Transactional
    public void modificar(MultipartFile archivo, String nombre, String idUsuario, String correo,
                                 String contrasenia, String direccion) throws MiException {

        validar(nombre, correo, contrasenia, direccion);

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

    private void validar(String nombre, String correo, String contrasenia, String direccion) throws MiException {

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

        if (direccion.isEmpty() || direccion == null) {
            throw new MiException("Debe ingresar una direccion");
        }
    }



    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(correo);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);

            if (usuario.getRol().toString().equals("PROVEEDOR")) {
                Proveedor proveedor = proveedorRepositorio.buscarPorEmail(usuario.getCorreo());
                session.setAttribute("usuariosession", proveedor);
                return new User(proveedor.getCorreo(), proveedor.getContrasenia(), permisos);

            } else if (usuario.getRol().toString().equals("CLIENTE")) {

                Cliente cliente = clienteRepositorio.buscarPorEmail(usuario.getCorreo());
                session.setAttribute("usuariosession", cliente);
                return new User(cliente.getCorreo(), cliente.getContrasenia(), permisos);

            } else if (usuario.getRol().toString().equals("ADMIN")) {

                session.setAttribute("usuariosession", usuario);
                return new User(usuario.getCorreo(), usuario.getContrasenia(), permisos);
            }
        }
            return null;
    }
}



   /* public void deshabilitarUsuario(String idUsuario) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setActivo(Boolean.FALSE);
            usuarioRepositorio.save(usuario);
        }

    }*/
