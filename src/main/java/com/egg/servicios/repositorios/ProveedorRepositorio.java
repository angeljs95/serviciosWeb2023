package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepositorio  extends JpaRepository<Proveedor,String> {


}
