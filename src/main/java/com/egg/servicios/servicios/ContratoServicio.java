package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Contrato;
import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.repositorios.ClienteRepositorio;
import com.egg.servicios.repositorios.ContratoRepositorio;
import com.egg.servicios.repositorios.ProveedorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ContratoServicio {

    @Autowired
    private ContratoRepositorio contratoRepositorio;

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;


    public void iniciarContrato( String idCliente, String idProveedor, String descripcion) {

        Optional<Cliente> respuesta= clienteRepositorio.findById(idCliente);
        Optional<Proveedor> respuesta1 = proveedorRepositorio.findById(idProveedor);
        if( respuesta.isPresent() && respuesta1.isPresent()) {
            Cliente cliente= respuesta.get();
            Proveedor proveedor= respuesta1.get();
            Contrato p= new Contrato();
            p.setCliente(cliente);
            p.setProveedor(proveedor);
            p.setEstadoPedido(true);
            p.setDescripcion(descripcion);
            contratoRepositorio.save(p);

        }










    }
}
