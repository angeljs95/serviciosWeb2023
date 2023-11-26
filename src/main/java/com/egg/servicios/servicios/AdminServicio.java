package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.egg.servicios.repositorios.AdminRepositorio;

@Service
public class AdminServicio {

    @Autowired
    private AdminRepositorio adminRepositorio;

    public Admin registrarAdministrador(String username, String password) {
//         Lógica para verificar si el username ya existe
//         Puedes realizar la validación y manejo de excepciones según tus necesidades
//
//         Hashear la contraseña antes de almacenarla
//        String hashedPassword;
//         Utilizar una función de hash segura (por ejemplo, BCrypt)
//        Admin nuevoAdmin = new Admin();
//        nuevoAdmin.setUsername(username);
//        nuevoAdmin.setPassword(hashedPassword);

//        return adminRepositorio.save(nuevoAdmin);
    }

    public Admin obtenerPorUsername(String username) {
        return adminRepositorio.findByUsername(username);
    }
}
