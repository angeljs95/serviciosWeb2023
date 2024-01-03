package com.egg.servicios.Entidades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.egg.servicios.enumeraciones.Profesiones;

import javax.persistence.*;

@Entity
@Table(name= "Proveedores")
@PrimaryKeyJoinColumn(name = "id")
public class Proveedor extends Usuario {

    private Double costoHora;
    private String matricula;
    private Integer cbu;
    private String descripcion;
    private Integer puntuacion;
    @ManyToOne
    @JoinColumn(name = "profesion_id") // Ajusta el nombre de la columna según tu esquema
    private Profesion profesion;
    @OneToMany
    private List<Comentario> comentarios;

    @ManyToMany(mappedBy = "proveedores")
    private Set<Cliente> clientes = new HashSet<>();
    @OneToMany ( fetch = FetchType.LAZY)
    private List<Contrato> contratoEnCursoP;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Contrato> ContratoFinalizadoP;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Imagen> imagenes; // Lista de imagenes para el album de muestra de trabajos realizados




                                      // GETTER AND SETTERS


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

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public Set<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Set<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Contrato> getContratoEnCursoP() {
        return contratoEnCursoP;
    }

    public void setContratoEnCursoP(List<Contrato> contratoEnCursoP) {
        this.contratoEnCursoP = contratoEnCursoP;
    }

    public List<Contrato> getContratosEnCursoP() {
        return contratoEnCursoP;
    }

    public void setContratosEnCursoP(List<Contrato> contratosEnCursoP) {
        this.contratoEnCursoP = contratosEnCursoP;
    }

    public List<Contrato> getContratoFinalizadoP() {
        return ContratoFinalizadoP;
    }

    public void setContratoFinalizadoP(List<Contrato> contratoFinalizadoP) {
        ContratoFinalizadoP = contratoFinalizadoP;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Profesion getProfesion() {
        return profesion;
    }

    public void setProfesion(Profesion profesion) {
        this.profesion = profesion;
    }
}
