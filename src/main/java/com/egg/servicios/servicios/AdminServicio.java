package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Admin;
import com.egg.servicios.Entidades.Imagen;
import com.egg.servicios.enumeraciones.Rol;
import com.egg.servicios.excepciones.MiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.egg.servicios.repositorios.AdminRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdminServicio {

    @Autowired
    private AdminRepositorio adminRepositorio;
    
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    @Transactional
    public void registrarAdministrador(MultipartFile archivo, String id, String nombre, String correo, String contrasenia, String contrasenia2, String direccion) throws MiException {
        
        validar(nombre, correo, contrasenia, contrasenia2,direccion);
        
        Admin admin = new Admin();
        
        admin.setNombre(nombre);
        admin.setCorreo(correo);
        admin.setDireccion(direccion);
        admin.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
        admin.setActivo(Boolean.TRUE);
        admin.setFechaAlta(new Date());
        admin.setRol(Rol.ADMIN);
        
        Imagen imagen = imagenServicio.guardar(archivo);
        admin.setImagen(imagen);

        adminRepositorio.save(admin);
    }
    
    @Transactional
    public void modificarAdmin(MultipartFile archivo, String nombre, String idAdmin, String correo,
            String contrasenia, String direccion) throws MiException {
        
        Optional<Admin> respuesta = adminRepositorio.findById(idAdmin);

        if (respuesta.isPresent()) {
            
            System.out.println("Estoy deontro de modificar cliente");
            Admin admin = respuesta.get();
            admin.setNombre(nombre);
            admin.setCorreo(correo);
            admin.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
            admin.setDireccion(direccion);
            

            String idImagen = null;
            if (admin.getImagen() != null) {
                idImagen = admin.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            admin.setImagen(imagen);
            
            adminRepositorio.save(admin);
        }
    }
    
    @Transactional(readOnly = true)
    public Admin getOne(String id) {
        return adminRepositorio.getOne(id);
    }

    /*public Admin registrarAdministrador(String username, String password) {
         Lógica para verificar si el username ya existe
         Puedes realizar la validación y manejo de excepciones según tus necesidades

         Hashear la contraseña antes de almacenarla
        String hashedPassword;
         Utilizar una función de hash segura (por ejemplo, BCrypt)
        Admin nuevoAdmin = new Admin();
        nuevoAdmin.setUsername(username);
        nuevoAdmin.setPassword(hashedPassword);

        return adminRepositorio.save(nuevoAdmin);
    }*/

    /*public Admin obtenerPorUsername(String username) {
        return adminRepositorio.findByUsername(username);
    }*/
    
    private void validar(String nombre, String correo,
            String contrasenia, String contrasenia2,
            String direccion) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El usuario no puede estar en blanco");
        }
        if (correo.isEmpty() || correo == null) {
            throw new MiException("El Correo no puede estar en blanco");
        }

        if (contrasenia.isEmpty() || contrasenia2.isEmpty() || contrasenia == null || contrasenia2 == null) {
            throw new MiException("La contraseña no puede estar vacia");
        } else if (!contrasenia.equals(contrasenia2)) {
            throw new MiException("Las contraseñas no coinciden");
        } else if (contrasenia.length() < 6) {
            throw new MiException("La contraseña no puede ser menor de 6 caracteres");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new MiException("Debe ingresar una direccion");
        }

    }
}
