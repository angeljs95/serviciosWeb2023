package com.egg.servicios.repositorios;

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


}
