package com.egg.servicios.repositorios;

import com.egg.servicios.Entidades.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepositorio extends JpaRepository<Admin, Long> {
    Admin findByUsername(String username);
}
