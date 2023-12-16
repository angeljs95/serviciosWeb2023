
package com.egg.servicios.Entidades;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name = "Clientes")
@PrimaryKeyJoinColumn(name = "id")
public class Cliente extends Usuario {

    private String barrio;
    private String metodoPago;

    @OneToMany
    private List<Comentario> comentarios;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "clientes_proveedores",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "proveedor_id")
    )
    private Set<Proveedor> proveedores= new HashSet<>();
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

    public Set<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(Set<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }
}
