package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Contrato;
import com.egg.servicios.Entidades.Proveedor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoRepositorio extends JpaRepository<Contrato, String> {
    
    @Query("SELECT c FROM Contrato c WHERE c.estadoPedido = true AND c.proveedor = :proveedor")
    public List<Contrato> findByEstadoPedidoAndProveedorId(@Param("proveedor") Proveedor proveedor);

}
