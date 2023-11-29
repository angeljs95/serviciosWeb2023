package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.*;
import com.egg.servicios.enumeraciones.Profesiones;
import com.egg.servicios.enumeraciones.Rol;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.ProveedorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
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

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class ProveedorServicio {

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearProveedor(MultipartFile archivo, String nombre, String correo, String contrasenia,
                               String contrasenia2, String direccion, Profesiones profesion,
                               Double costoXHora, String descripcion) throws MiException {

        validar(nombre, correo, contrasenia, contrasenia2, direccion, profesion, costoXHora);

        Proveedor proveedor = new Proveedor();

        //seteamos primero los datos de usuario

        proveedor.setNombre(nombre);
        proveedor.setDireccion(direccion);
        proveedor.setCorreo(correo);
        proveedor.setFechaAlta(new Date());
        proveedor.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
        proveedor.setRol(Rol.PROVEEDOR);
        proveedor.setActivo(true);

        //seteamos los datos de proveedor
        proveedor.setProfesion(profesion);
        proveedor.setCbu(null);
        proveedor.setCostoHora(costoXHora);
        proveedor.setMatricula(null);
        proveedor.setPuntuacion(0);
        proveedor.setComentarios(new ArrayList<>());
        proveedor.setClientes(new ArrayList<>());
        proveedor.setDescripcion(descripcion);
        proveedor.setTrabajosEnCurso(new ArrayList<>());

        Imagen imagen = imagenServicio.guardar(archivo);
        proveedor.setImagen(imagen);
        proveedorRepositorio.save(proveedor);
    }


    public void eliminarProveedor(String idProveedor) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.setActivo(false);
            proveedorRepositorio.save(proveedor);

        }
    }

    @Transactional
    public void cambiarEstado(String id) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();

            if (proveedor.getActivo().equals(true)) {
                proveedor.setActivo(false);
            } else if (proveedor.getActivo().equals(false)) {
                proveedor.setActivo(true);
            }
        }
    }


    @Transactional
    public void modificarProveedor(MultipartFile archivo, String nombre, String correo, String contrasenia,
                                   String contrasenia2, String direccion, Profesiones profesion,
                                   Double costoXHora, String idProveedor) throws MiException {

        validar(nombre, correo, contrasenia, contrasenia2, direccion, profesion, costoXHora);

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.setNombre(nombre);
            proveedor.setDireccion(direccion);
            proveedor.setCorreo(correo);
            proveedor.setFechaAlta(new Date());
            proveedor.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
            proveedor.setActivo(true);
            proveedor.setProfesion(profesion);
//            proveedor.setCbu(cbu);
            proveedor.setCostoHora(costoXHora);
//            proveedor.setMatricula(matricula);
            String idImagen = null;
            if (proveedor.getImagen() != null) {
                idImagen = proveedor.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            proveedor.setImagen(imagen);
            proveedorRepositorio.save(proveedor);

        }
    }

    @Transactional(readOnly = true)
    public List listarProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        proveedores = proveedorRepositorio.findAll();
        return proveedores;
    }

    public List<Proveedor> puntuacionP(Integer puntuacion) {
        List<Proveedor> puntuaciones = (List<Proveedor>) proveedorRepositorio.buscarPorPuntuacion(puntuacion);
        return puntuaciones;
    }

    public List proveedoresActivos() {
        List<Proveedor> estadosActivos = proveedorRepositorio.obtenerPerfilesActivos();
        return estadosActivos;
    }

    public List proveedoresInactivos() {
        List<Proveedor> estadosInactivos = proveedorRepositorio.obtenerPerfilesInactivos();
        return estadosInactivos;
    }

    public Proveedor getOne(String id) {
        return proveedorRepositorio.getOne(id);
    }


    private void validar(String nombre, String correo, String contrasenia, String contrasenia2, String direccion,
                         Profesiones profesion, /*Integer cbu,*/ Double costoXHora /*, String matricula*/) throws MiException {

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

        if (profesion == null) {
            throw new MiException("Debe añadir una profesion");
        }
//
//        if (cbu == null) {
//            throw new MiException("Debe un cbu para registrar su pago");
//        }

        if (costoXHora == null) {
            throw new MiException("Debe ingresar un monto base de Honorarios");
        }

//        if (matricula.isEmpty() || matricula == null) {
//            throw new MiException("Debe ingresar su matricula para continuar");
//        }

    }

    public List listarProfesiones() {

        List<Profesiones> profesiones = Arrays.asList(Profesiones.values());
        return profesiones;
    }

    public void crearAlbum(MultipartFile archivo, String idProveedor) throws MiException {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            List<Imagen> album = new ArrayList<>();// (List<Imagen>) imagenServicio.agregarImagen(archivo);
            Imagen imagen = imagenServicio.guardar(archivo);
            album.add(imagen);

            proveedor.setImagenes(album);
            proveedorRepositorio.save(proveedor);
        }
    }

    public void calificacion(Integer puntuacion, String idProveedor) {

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            if (puntuacion > 0 && puntuacion < 6) {
                proveedor.setPuntuacion(puntuacion);
                proveedorRepositorio.save(proveedor);
            }
        }
    }
/*
    public void traEncurso(Pedido pedido) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(pedido.getProveedor().getId());
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            List<Pedido> contratoIniciado = new ArrayList<>();
            contratoIniciado.add(pedido);
            proveedor.setTrabajosEnCurso(contratoIniciado);
            proveedorRepositorio.save(proveedor);

        }

    }

   public void traTerminado(Pedido pedido) {

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(pedido.getProveedor().getId());
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            List<Pedido> contratoTerminado = new ArrayList<>();
            pedido.setEstadoPedido(false);
            contratoTerminado.add(pedido);
            proveedor.setTrabajosTerminados(contratoTerminado);
            proveedorRepositorio.save(proveedor);

        }


    }*/

    @Transactional
    public void tareasEnCurso(Contrato contrato, String idProveedor) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.getTrabajosEnCurso().add(contrato);
            proveedorRepositorio.save(proveedor);

        }
    }

    public void tareasTerminadas(Contrato contrato, String idProveedor) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            List<Contrato> lista = proveedor.getTrabajosEnCurso();

            if (lista.contains(contrato)) {
                lista.remove(contrato);
                List<Contrato> trabajoFinalizado = new ArrayList<>();
                trabajoFinalizado.add(contrato);
                proveedor.setTrabajosEnCurso(lista);
                proveedor.setContratoFinalizado(trabajoFinalizado);
                proveedorRepositorio.save(proveedor);
            }

        }

    }


}


     /*@Transactional
    public void modificarProveedor(Proveedor proveedore) {

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(proveedore.getId());

        if (respuesta.isPresent()) {
            proveedorRepositorio.save(respuesta.get());
        }
    }*/



