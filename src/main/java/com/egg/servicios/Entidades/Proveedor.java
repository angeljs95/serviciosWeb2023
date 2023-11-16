
package com.egg.servicios.Entidades;

import java.util.ArrayList;
import com.egg.servicios.Entidades.Cliente;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import javax.persistence.Entity;



@Entity
public class Proveedor extends Usuario {

    @Enumerated(EnumType.STRING)
   protected Enum profesion;
    protected Double costoHora;

    protected  String matricula;
    protected Integer cbu;
    protected Integer puntuacion;
    protected ArrayList<Comentario> comentarios;
    protected ArrayList<Cliente> clientes;
    protected String descripcion;


    public Proveedor() {
        super();
    }


    public Enum getProfesion() {
        return profesion;
    }

    public void setProfesion(Enum profesion) {
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
