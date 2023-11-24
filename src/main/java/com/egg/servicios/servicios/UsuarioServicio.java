package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Imagen;
import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.enumeraciones.Rol;
import com.egg.servicios.excepciones.MiException;
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


    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List listarUsuarios() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    public void eliminarUsuario(String idUsuario) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setActivo(Boolean.FALSE);
            usuarioRepositorio.save(usuario);
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
            session.setAttribute("usuariosession", usuario);


            return new User(usuario.getCorreo(), usuario.getContrasenia(), permisos);
        } else {
            return null;
        }
    }





}
