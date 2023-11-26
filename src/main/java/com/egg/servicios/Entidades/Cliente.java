package com.egg.servicios.Entidades;

import java.util.ArrayList;
import javax.persistence.Entity;

@Entity
public class Cliente extends Usuario {

    private String barrio;
    private String metodoPago;
    private ArrayList<Comentario> comentarios;
    private ArrayList<Proveedor> proveedores;

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

    public ArrayList<Comentario> getComentarioss() {
        return comentarios;

    }

    public void setComentarios(ArrayList<Comentario> comentarioss) {
        this.comentarios = comentarios;
    }

    public ArrayList<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(ArrayList<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }
}
