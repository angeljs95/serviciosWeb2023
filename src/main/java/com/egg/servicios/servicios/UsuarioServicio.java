package com.egg.servicios.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearUsuario(MultipartFile archivo, String nombre, String correo,
                             String contrasenia, String contrasenia2, String direccion) throws MiException {

        //validar(nombre, correo, contrasenia, contrasenia2, direccion);
        validar(nombre, correo, contrasenia, contrasenia2);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setDireccion(direccion);
        usuario.setCorreo(correo);
        usuario.SetContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
        usuario.setRol(Rol.User);
        usuario.setActivo(true);


        // Imagen imagen= imagenServicio.guardar(archivo);
        //usuario.setImagen(imagen);
        usuarioRepositorio.save(usuario);

    }
@Transactional
public void modificarUsuario(MultipartFile archivo, String nombre, String idUsuario, String correo,
                             String contrasenia, String contrasenia2, String direccion) throws MiException {

        validar(nombre, correo, contrasenia, contrasenia2);

        Optional<Usuario> respuesta= usuarioRepositorio.findById(idUsuario);

        if(respuesta.isPresent()){
            Usuario usuario= respuesta.get();
            usuario.setNombre(nombre);
            usuario.setDireccion(direccion);
            usuario.setCorreo(correo);
            usuario.SetContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
            String idImagen= null;
            if(usuario.getImagen()!= null){
                idImagen= usuario.getImagen().getId();
            }

            Imagen imagen= imagenService.actualizar(archivo, idImagen);
            usuario.setImagen(imagen);
            usuarioRepositorio.save(usuario);
        }

        public Usuario getOne(String id){
            return usuarioRepositorio.getOne(id);
    }


}







    private void validar(String nombre, String correo, String contrasenia, String contrasenia2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El usuario no puede estar en blanco");
        }
        if (correo.isEmpty() || correo == null) {
            throw new MiException("El Correo no puede estar en blanco");
        }
        if (contrasenia.isEmpty() || contrasenia == null || contrasenia.length() <= 6) {
            throw new MiException("La contraseña no puede estar vacia, ni tener menos de 6 caracteres");

            if (contrasenia != contrasenia2) {
                throw new MiException("La contraseña no coincide");
            }

          /*  if (direccion.isEmpty()|| direccion == null){
                throw new MiException("Debe ingresar una direccion");
            }*/
        }

    }

}
