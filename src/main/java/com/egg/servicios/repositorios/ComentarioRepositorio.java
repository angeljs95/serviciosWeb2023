package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepositorio extends JpaRepository<Comentario, String> {

}
