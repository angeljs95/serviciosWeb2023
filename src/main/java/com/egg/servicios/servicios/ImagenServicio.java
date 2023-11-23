package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Comentario;
import com.egg.servicios.Entidades.Imagen;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.ImagenRepositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepositorio imagenRepositorio;

    public Imagen guardar(MultipartFile archivo) throws MiException {

        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }

        }
        return null;
    }

    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException {

        if (archivo != null) {
            Imagen imagen = new Imagen();
            try {
                if (idImagen != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    }
                }

                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        return null;
    }

    //metodo para guardar fotos en el album de trabajo de un proveedor
    @Transactional
    public Imagen agregarImagen(MultipartFile archivo) throws MiException {
        Imagen imagen = guardar(archivo);
        List<Imagen> album= new ArrayList<>();
        album.add(imagen);

        return (Imagen) album;
    }



}
