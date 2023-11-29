
package com.egg.servicios.servicios;

import com.egg.servicios.Entidades.Cliente;
import com.egg.servicios.Entidades.Comentario;
import com.egg.servicios.Entidades.ComentarioAux;
import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.repositorios.ClienteRepositorio;
import com.egg.servicios.repositorios.ComentarioRepositorio;
import com.egg.servicios.repositorios.ProveedorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComentarioServicio {
    
    @Autowired
    private ComentarioRepositorio comentarioRepositorio;
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
@Transactional
public Comentario crearComentario(ComentarioAux comentarioaux ){

    Proveedor proveedor = proveedorRepositorio.findById(comentarioaux.getIdProveedor())
            .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con ID: " + comentarioaux.getIdProveedor()));

    Cliente cliente = clienteRepositorio.findById(comentarioaux.getIdCliente())
            .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + comentarioaux.getIdCliente()));

    Comentario coment = new Comentario();
    coment.setComentario(comentarioaux.getComentario());
    coment.setCliente(cliente);
    coment.setProveedor(proveedor);
    comentarioRepositorio.save(coment);

    return coment;
}

/*public List<Comentario> cometariosAlProveedor(String idProveedor, String comentario) {
 Optional<Proveedor> respuesta= proveedorRepositorio.findById(idProveedor);
    if (respuesta.isPresent()){
        Proveedor proveedor= respuesta.get();
        Comentario comentarioP= crearComentario(comentario);
        List<Comentario> come= new ArrayList<>();
        come.add(comentarioP);
        proveedor.setComentarios(come);

    }
}*/

}
