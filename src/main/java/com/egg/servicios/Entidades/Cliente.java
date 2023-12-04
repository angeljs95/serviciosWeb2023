
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
    
    @OneToMany (fetch = FetchType.LAZY)
    private List<Proveedor> proveedores;
    @OneToMany (fetch = FetchType.LAZY)
    private List<Contrato> contratoEnCursoC;
    @OneToMany (fetch = FetchType.LAZY)
    private List<Contrato> ContratoFinalizadoC;

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Contrato> getContratoEnCursoC() {
        return contratoEnCursoC;
    }

    public void setContratoEnCursoC(List<Contrato> contratoEnCursoC) {
        this.contratoEnCursoC = contratoEnCursoC;
    }

    public List<Contrato> getContratoFinalizadoC() {
        return ContratoFinalizadoC;
    }

    public void setContratoFinalizadoC(List<Contrato> contratoFinalizadoC) {
        ContratoFinalizadoC = contratoFinalizadoC;
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
