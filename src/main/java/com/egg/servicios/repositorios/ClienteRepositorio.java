
package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {

    
    @Query("SELECT l FROM Cliente l WHERE l.correo = :correo")
    public Cliente buscarPorEmail(@Param("correo") String correo);
    
    @Query("SELECT l FROM Cliente l where l.direccion = :direccion")
    public Cliente buscarPorDireccion(@Param("direccion") String direccion);
    
    @Query("SELECT l FROM Cliente l where l.barrio = :barrio")
    public Cliente buscarPorBarrio(@Param("barrio") String barrio);
    
    

}
