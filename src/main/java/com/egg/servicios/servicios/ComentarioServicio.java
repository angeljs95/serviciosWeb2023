
package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Comentario;
import com.egg.servicios.repositorios.ComentarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComentarioServicio {
    
    @Autowired
    private ComentarioRepositorio comentarioRepositorio;
    
@Transactional
public Comentario crearComentario(String comentario){
    
    Comentario coment = new Comentario();
    
    coment.setComentario(comentario);
    
    comentarioRepositorio.save(coment);
    
    return coment;
}
    
}
