package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, String> {

    @Query("SELECT p FROM Proveedor p WHERE p.correo = :correo")
    public Proveedor buscarPorEmail(@Param("correo") String correo);

    @Query("SELECT p FROM Proveedor p where p.direccion = :direccion")
    public Proveedor buscarPorDireccion(@Param("direccion") String direccion);

}
