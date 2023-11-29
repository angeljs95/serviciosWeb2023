package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoRepositorio extends JpaRepository<Contrato, String> {


}
