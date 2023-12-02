package com.egg.servicios.Entidades;

import java.util.ArrayList;
import java.util.List;

import com.egg.servicios.enumeraciones.Profesiones;

import javax.persistence.*;

@Entity
@Table(name= " Proveedores")
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
    @OneToMany(fetch = FetchType.LAZY)
    private List<Cliente> clientes;
    @OneToMany ( fetch = FetchType.LAZY)
    private List<Contrato> contratosEnCurso;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Contrato> ContratoFinalizado;
    @OneToMany
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

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Contrato> getContratosEnCurso() {
        return contratosEnCurso;
    }

    public void setContratosEnCurso(List<Contrato> contratosEnCurso) {
        this.contratosEnCurso = contratosEnCurso;
    }

    public List<Contrato> getContratoFinalizado() {
        return ContratoFinalizado;
    }

    public void setContratoFinalizado(List<Contrato> contratoFinalizado) {
        ContratoFinalizado = contratoFinalizado;
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
