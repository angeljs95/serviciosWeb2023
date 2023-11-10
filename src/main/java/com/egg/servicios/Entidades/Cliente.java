
package com.egg.servicios.Entidades;

import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.Entidades.Usuario;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Entity;


@Entity
public class Cliente extends Usuario{
    
    
    String barrio;
    String metodoPago;
    
    ArrayList<String> comentarios;
    ArrayList<Proveedor> proveedores;


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

    public ArrayList<String> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<String> comentarios) {
        this.comentarios = comentarios;
    }

    public ArrayList<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(ArrayList<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }
    
    
    
}
