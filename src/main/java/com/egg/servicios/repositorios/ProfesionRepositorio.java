package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Profesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfesionRepositorio extends JpaRepository<Profesion, String> {

    @Query("SELECT l FROM Profesion l WHERE l.profesion = :profesion")
    public Profesion buscarProfesion(@Param("profesion") String profesion);


}
