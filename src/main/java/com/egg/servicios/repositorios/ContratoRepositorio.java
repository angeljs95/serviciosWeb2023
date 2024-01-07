package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Contrato;
import com.egg.servicios.Entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoRepositorio extends JpaRepository<Contrato, String> {


    //  @Query("SELECT l FROM Contratos l WHERE l.proveedor_id = :id and l.estado_pedido = true")
    //public List<Contrato> contratosActivos(@Param("id") String id);

    // List<Contrato> findByEstadoPedidoAndProveedorIdTrue(String proveedorId);

    @Query("SELECT c FROM Contrato c WHERE c.estadoPedido = true AND c.proveedor = :proveedor")
    List<Contrato> findByEstadoPedidoAndProveedorId(@Param("proveedor") Proveedor proveedor);

    @Query("SELECT c FROM Contrato c WHERE c.estadoPedido = true AND c.proveedor = :proveedor  AND c.cliente = :cliente")
    public Contrato findByEstadoPedidoAndProveedorAndCliente(@Param("proveedor") Proveedor proveedor, @Param("cliente") Cliente cliente);

    @Query("SELECT c FROM Contrato c WHERE c.cliente = :cliente AND c.proveedor = :proveedor")
    public Contrato findContratosByClienteAndProveedor(@Param("cliente") Cliente cliente, @Param("proveedor") Proveedor proveedor);

    //@Query("SELECT c FROM Contrato c WHERE c.cliente = :cliente AND c.proveedor = :proveedor")
    public boolean existsByClienteAndProveedor(Cliente cliente, Proveedor proveedor);

    @Query("SELECT c FROM Contrato c WHERE c.estadoPedido = false AND c.proveedor = :proveedor  AND c.cliente = :cliente")
    List<Contrato> findByEstadoPedidoAndProveedorAndClienteFalse(@Param("proveedor") Proveedor proveedor, @Param("cliente") Cliente cliente);


    @Query("SELECT c FROM Contrato c WHERE c.estadoPedido = true AND c.proveedor = :proveedor AND c.cliente = :cliente")
    List<Contrato> exisTrue(@Param("proveedor") Proveedor proveedor, @Param("cliente") Cliente cliente);
}
