
package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    //buscar por el correo y que este activo
    @Query("SELECT l FROM Usuario l WHERE l.correo = :correo")
    public Usuario buscarPorEmail(@Param("correo") String correo);
    
    @Query("SELECT l FROM Usuario l where l.direccion = :direccion")
    public Usuario buscarPorDireccion(@Param("direccion") String direccion);
    
}
    

