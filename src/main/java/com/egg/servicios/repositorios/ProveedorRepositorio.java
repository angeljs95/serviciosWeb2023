package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepositorio  extends JpaRepository<Proveedor,String> {
    
    
    @Query("SELECT l FROM Cliente l WHERE l.correo = :correo")
    public Proveedor buscarPorEmail(@Param("correo") String correo);
    
    @Query("SELECT l FROM Cliente l WHERE l.puntuacion = :puntuacion")
    public Proveedor buscarPorPuntuacion(@Param("puntuacion") String puntuacion);

    @Query("SELECT l FROM Cliente l WHERE l.profesion = :profesion")
    public Proveedor buscarPorProfesion(@Param("profesion") String profesion);
    
    @Query("SELECT l FROM Cliente l WHERE l.costoHora = :costoHora")
    public Proveedor buscarPorCostoHora(@Param("costoHora") String costoHora);

    @Query("SELECT p FROM Proveedor p where p.direccion = :direccion")
    public Proveedor buscarPorDireccion(@Param("direccion") String direccion);


}
