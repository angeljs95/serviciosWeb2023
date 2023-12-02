package com.egg.servicios.servicios;


import com.egg.servicios.Entidades.*;
import com.egg.servicios.repositorios.ProfesionRepositorio;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfesionServicio {

    @Autowired
    ProfesionRepositorio profesionRepositorio;

    // PARA CREAR UNA PROFESION
    @Transactional
    public Profesion crearProfesion(String profesion){

        Profesion nuevaProfesion= new Profesion();

        nuevaProfesion.setProfesion(profesion);
        nuevaProfesion.setActivo(true);
        profesionRepositorio.save(nuevaProfesion);

        return nuevaProfesion;
    }
    // PARA DEVOLVER UNA LISTA DE TODAS LAS PROFESIONES
    @Transactional(readOnly = true)
    public List listarProfesiones() {
        List<Profesion> profesiones = new ArrayList();
        profesiones = profesionRepositorio.findAll();
        return profesiones;
    }
    // MODIFICAR PROFESION
    @Transactional
    public Profesion modificarProfesion(String idProfesion){

        Optional<Profesion> respuesta = profesionRepositorio.findById(idProfesion);

        Profesion profesionModificada = respuesta.get();

        profesionRepositorio.save(profesionModificada);

        return profesionModificada;
    }

    // PARA ELIMINAR UNA PROFESION

    @Transactional
    public void eliminarProfesion(String idProfesion){

        Optional<Profesion> respuesta = profesionRepositorio.findById(idProfesion);
        Profesion profesion = respuesta.get();
        profesion.setActivo(false);
        profesionRepositorio.save(profesion);

    }

    public Profesion buscarProfesion(String prof){
        Profesion profesion= profesionRepositorio.buscarProfesion(prof);
        return profesion;

    }


}
