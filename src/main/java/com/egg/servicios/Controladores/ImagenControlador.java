package com.egg.servicios.Controladores;

import com.egg.servicios.Entidades.Imagen;
import com.egg.servicios.Entidades.Proveedor;
import com.egg.servicios.Entidades.Usuario;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.ProveedorRepositorio;
import com.egg.servicios.servicios.ProveedorServicio;
import com.egg.servicios.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {

    @Autowired
    UsuarioServicio usuarioServicio;
    @Autowired
    ProveedorServicio proveedorServicio;
    @Autowired
    ProveedorRepositorio proveedorRepositorio;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) throws MiException {
        Usuario usuario = usuarioServicio.getOne(id);
        byte[] imagen = usuario.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

   /* @GetMapping("/imagenes/{id}")
    @ResponseBody
    public ResponseEntity<List<byte[]>> imagenes ( @PathVariable String id) {

        Proveedor proveedor= proveedorServicio.getOne(id);
        int i;
        List<byte[]> imagenes= new ArrayList<byte[]>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        for (i=0; i<proveedor.getImagenes().size(); i++) {
            Imagen img = proveedor.getImagenes().get(i);
            imagenes.add( img.getContenido());
            System.out.println("se agrego");
        }
        System.out.println(" salio con " + imagenes.size()+ " imaenes");
        System.out.println(imagenes.toString());
        return new ResponseEntity<>(imagenes, HttpStatus.OK);

    }*/

    @GetMapping("/imagenes/{id}")
    @ResponseBody
    public List<byte[]> imagenes ( @PathVariable String id) {

        Proveedor proveedor= proveedorServicio.getOne(id);
        int i;
        List<byte[]> imagenes= new ArrayList<byte[]>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        for (i=0; i<proveedor.getImagenes().size(); i++) {
            Imagen img = proveedor.getImagenes().get(i);
            imagenes.add( img.getContenido());
            System.out.println("se agrego");
        }
        System.out.println(" salio con " + imagenes.size()+ " imaenes");
        System.out.println(imagenes.toString());
        return imagenes;

    }

    /*@GetMapping("/perfil/album/{id}")
    public ResponseEntity<byte[]> albumUsuario(@PathVariable String id) throws MiException {
        Proveedor proveedor = proveedorServicio.getOne(id);
        List<Imagen> imagenes = proveedorRepositorio.findAllImagenes(id);
        byte[] s= imagenUsuario(id).getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
       ResponseEntity<byte[]> lcuestion;
        lcuestion= new ResponseEntity<>(s,headers,HttpStatus.OK);
        return lcuestion;
    }*/

   /* public ResponseEntity <List<byte[]>> imagenes (@PathVariable String id){
        Proveedor proveedor= proveedorServicio.getOne(id);
        byte[] photo = proveedor.getImagen().getContenido();
        String bphoto = Base64.getEncoder().encodeToString(photo);
    List<Imagen> a= proveedorRepositorio.findAllImagenes(id);
    int i;
        List<byte[]> lista= new ArrayList<>();

        for (i=0; i<a.size(); i++){
            Imagen t= a.get(i);
             lista.add(t.getContenido());
            System.out.println("agregado" + "1");
            System.out.println(t.getId());
        }
        System.out.println("salio");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        System.out.println("creo el heders");
return new ResponseEntity<>(lista, headers, HttpStatus.OK);
    }*/

    /*public ResponseEntity<List<String>> strimeo(@PathVariable String id) {
        Proveedor proveedor= proveedorServicio.getOne(id);

        List<Imagen> a= proveedor.getImagenes();
        int i;
        List<String> listaString= new ArrayList<>();

        for (i=0; i<a.size(); i++){
            Imagen t= a.get(i);
            String bphoto = Base64.getEncoder().encodeToString(t.getContenido());
            System.out.println("iterado");
            listaString.add(bphoto);
            System.out.println("agregado" + "1");
            System.out.println(t.getId());
        }
        System.out.println("salio");
        HttpHeaders headers =  new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        System.out.println("creo el heders");
        return new ResponseEntity<List<String>>(listaString, headers, HttpStatus.OK);
    }*/

 /* @GetMapping("/imagenes/{id}")
public ResponseEntity<List<Imagen>> imageness(@PathVariable String id){

       Proveedor proveedor= proveedorServicio.getOne(id);
       List<Imagen> img= proveedor.getImagenes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        if (!img.isEmpty()) {
            System.out.println("paso");
            return new ResponseEntity<>(img,headers, HttpStatus.OK);
        } else {
            System.out.println("oligwi");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }}*/






   /* public ResponseEntity <List<Imagen>> imagenes(@PathVariable String id) {
        Proveedor proveedor = proveedorServicio.getOne(id);
        List<Imagen> img = proveedorRepositorio.findAllImagenes(id);


    return new ResponseEntity<>(img,HttpStatus.OK);


    }*/
}
