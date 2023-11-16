
package com.egg.servicios.Entidades;


import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.Entidades.Usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;


@Entity
public class Cliente extends Usuario {

    protected String barrio;
    protected String metodoPago;

    protected ArrayList<String> comentarioss;
    protected ArrayList<Proveedor> proveedores;


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


    public ArrayList <String> getComentarioss() {
        return comentarioss;
    }

    public void setComentarioss(ArrayList<String> comentarioss) {
        this.comentarioss = comentarioss;
    }

    public ArrayList <Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(ArrayList<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }
}
