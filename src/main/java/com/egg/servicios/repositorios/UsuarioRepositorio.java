
package com.egg.servicios.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    
    @Query("SELECT l FROM Usuario l WHERE l.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);
    
    @Query("SELECT l FROM Usuario l where l.direccion = :direccion")
    public Usuario buscarPorDireccion(@Param("direccion") String direccion);
    
}
    

