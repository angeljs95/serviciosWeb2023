
package com.egg.servicios.Entidades;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.*;


@Entity
@Table(name = "Clientes")
public class Cliente extends Usuario {

    private String barrio;
    private String metodoPago;

@OneToMany
    private List<Comentario> comentarios;
@OneToMany
    private List<Proveedor> proveedores;
    @ElementCollection
private List<Contrato> contratoEnCurso;
    @ElementCollection
    private List<Contrato> ContratoFinalizado;

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Contrato> getContratoEnCurso() {
        return contratoEnCurso;
    }

    public void setContratoEnCurso(List<Contrato> contratoEnCurso) {
        this.contratoEnCurso = contratoEnCurso;
    }

    public List<Contrato> getContratoFinalizado() {
        return ContratoFinalizado;
    }

    public void setContratoFinalizado(List<Contrato> contratoFinalizado) {
        ContratoFinalizado = contratoFinalizado;
    }
    public Cliente() {
        super();
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }


    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarioss(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }
}
