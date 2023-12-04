package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Contrato;
import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.repositorios.ClienteRepositorio;
import com.egg.servicios.repositorios.ContratoRepositorio;
import com.egg.servicios.repositorios.ProveedorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContratoServicio {

    @Autowired
    private ContratoRepositorio contratoRepositorio;

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
    @Transactional
    public Contrato crearContrato(String idCliente, String idProveedor, String descripcion) {

        Optional<Cliente> respuesta = clienteRepositorio.findById(idCliente);
        Optional<Proveedor> respuesta1 = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent() && respuesta1.isPresent()) {
            Cliente cliente = respuesta.get();
            Proveedor proveedor = respuesta1.get();
            Contrato p = new Contrato();
            p.setFechaEdicion(new Date());
            p.setNombreCliente(cliente.getNombre());
            p.setCliente(cliente);
            p.setProveedor(proveedor);
            p.setEstadoPedido(true);
            p.setDescripcion(descripcion);
            contratoRepositorio.save(p);
            return p;
        }
        return null;
    }
    
    @Transactional
    public List<Contrato> obtenerContratosActivos(Proveedor proveedor) {
        return contratoRepositorio.findByEstadoPedidoAndProveedorId(proveedor);
    }
    @Transactional
    public Contrato obtenerContratoactivo(Proveedor proveedor, Cliente cliente) {
        return contratoRepositorio.findByEstadoPedidoAndProveedorIdUno(proveedor);
    }
    
    @Transactional
    public Contrato obtenerContrato(Proveedor proveedor, Cliente cliente) {
       return contratoRepositorio.findContratosByClienteAndProveedor(cliente, proveedor);
    }
    
  /*  @Transactional
    public void eliminarContrato(Proveedor proveedor, Cliente cliente) {
      // Contrato contrato = obtenerContrato(proveedor, cliente);
       contrato.setEstadoPedido(false);
       contratoRepositorio.save(contrato);

    }*/

    @Transactional
    public Contrato eliminarContrato(Contrato contrato) {

        if(contrato!= null)
        contrato.setEstadoPedido(false);
        contratoRepositorio.save(contrato);
return contrato;
    }

}
