package com.egg.servicios.Entidades;

import java.util.ArrayList;
import com.egg.servicios.enumeraciones.Profesiones;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

@Entity
public class Proveedor extends Usuario {

    @Enumerated(EnumType.STRING)
    private Profesiones profesion;
    private Double costoHora;
    private String matricula;
    private Integer cbu;
    private Integer puntuacion;
    @OneToMany
    private ArrayList<Comentario> comentarios;
    @OneToMany
    private ArrayList<Cliente> clientes;
    private String descripcion;

    public Proveedor() {
        super();
    }

    public Profesiones getProfesion() {

        return profesion;
    }

    public void setProfesion(Profesiones profesion) {
        this.profesion = profesion;
    }

    public Double getCostoHora() {
        return costoHora;
    }

    public void setCostoHora(Double costoHora) {
        this.costoHora = costoHora;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getCbu() {
        return cbu;
    }

    public void setCbu(Integer cbu) {
        this.cbu = cbu;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
