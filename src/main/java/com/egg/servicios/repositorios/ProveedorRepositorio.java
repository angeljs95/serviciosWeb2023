package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Imagen;
import com.egg.servicios.Entidades.Profesion;
import com.egg.servicios.Entidades.Proveedor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepositorio  extends JpaRepository<Proveedor,String> {
    
    
    @Query("SELECT l FROM Proveedor l WHERE l.correo = :correo")
    public Proveedor buscarPorEmail(@Param("correo") String correo);
    
    @Query("SELECT l FROM Proveedor l WHERE l.puntuacion = :puntuacion")
    public Proveedor buscarPorPuntuacion(@Param("puntuacion") Integer puntuacion);

    @Query("SELECT l FROM Proveedor l WHERE l.profesion = :profesion")
    public Proveedor buscarPorProfesion(@Param("profesion") String profesion);
    
    @Query("SELECT l FROM Proveedor l WHERE l.costoHora = :costoHora")
    public Proveedor buscarPorCostoHora(@Param("costoHora") String costoHora);

    @Query("SELECT p FROM Proveedor p where p.direccion = :direccion")
    public Proveedor buscarPorDireccion(@Param("direccion") String direccion);

    @Query("SELECT l FROM Proveedor l WHERE l.activo = : true")
    public List<Proveedor> obtenerPerfilesActivos();

    @Query("SELECT l FROM Proveedor l WHERE l.activo = :false")
    public List<Proveedor> obtenerPerfilesInactivos();

    @Query("SELECT l FROM Proveedor l WHERE l.profesion = :profesion")
    List<Proveedor> findByProfesion(@Param("profesion") Profesion profesion);

    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    List<Proveedor> findByDireccionContainingIgnoreCase(String direccion);
   // @Query("SELECT p FROM Proveedor p ORDER BY nombre asc")
    List<Proveedor> findAllByOrderByNombreAsc();
    List<Proveedor> findAllByOrderByNombreDesc();

    @Query("SELECT l.imagenes FROM Proveedor l WHERE l.id = :id")
    public List<Imagen>  findAllImagenes (@Param("id") String id);



}
