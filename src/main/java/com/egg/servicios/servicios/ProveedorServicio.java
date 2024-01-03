package com.egg.servicios.servicios;


import com.egg.servicios.Entidades.*;
import com.egg.servicios.enumeraciones.Rol;
import com.egg.servicios.excepciones.MiException;
import com.egg.servicios.repositorios.ContratoRepositorio;
import com.egg.servicios.repositorios.ProfesionRepositorio;
import com.egg.servicios.repositorios.ProveedorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ProveedorServicio {

    @Autowired
    private ProveedorRepositorio proveedorRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private ContratoRepositorio contratoRepositorio;
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private ProfesionServicio profesionServicio;

    @Autowired
    private ContratoServicio contratoServicio;
    @Autowired
    private ProfesionRepositorio profesionRepositorio;

    @Transactional
    public void crearProveedor(MultipartFile archivo, String nombre, String correo, String contrasenia,
                               String contrasenia2, String direccion, String profesion,
                               Double costoXHora, String descripcion) throws MiException {

        validar(nombre, correo, contrasenia, contrasenia2, direccion, profesion, costoXHora);

        Proveedor proveedor = new Proveedor();

        //seteamos primero los datos de usuario
        //seteamos primero los datos de usuario
        proveedor.setNombre(nombre);
        proveedor.setDireccion(direccion);
        proveedor.setCorreo(correo);
        proveedor.setFechaAlta(new Date());
        proveedor.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
        proveedor.setRol(Rol.PROVEEDOR);
        proveedor.setActivo(true);

        //seteamos los datos de proveedor
        Profesion profesion1 = profesionServicio.buscarProfesion(profesion);
        proveedor.setProfesion(profesion1);
        proveedor.setCbu(null);
        proveedor.setCostoHora(costoXHora);
        proveedor.setMatricula(null);
        proveedor.setPuntuacion(0);
        proveedor.setComentarios(new ArrayList<>());
        proveedor.setClientes(new HashSet<>());
        proveedor.setDescripcion(descripcion);
        proveedor.setContratosEnCursoP(new ArrayList<>());

        Imagen imagen = imagenServicio.guardar(archivo);
        proveedor.setImagen(imagen);
        proveedorRepositorio.save(proveedor);
    }


    public void eliminarProveedor(String idProveedor) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.setActivo(false);
            proveedorRepositorio.save(proveedor);

        }
    }

    @Transactional
    public void cambiarEstado(String id) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();

            if (proveedor.getActivo().equals(true)) {
                proveedor.setActivo(false);
            } else if (proveedor.getActivo().equals(false)) {
                proveedor.setActivo(true);
            }
        }
    }


    @Transactional
    public Proveedor modificarProveedor(MultipartFile archivo, String nombre, String correo, String contrasenia,
                                        String contrasenia2, String direccion, String profesion,
                                        Double costoXHora, String idProveedor) throws MiException {

        validar(nombre, correo, contrasenia, contrasenia2, direccion, profesion, costoXHora);

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.setNombre(nombre);
            proveedor.setDireccion(direccion);
            proveedor.setCorreo(correo);
            proveedor.setFechaAlta(new Date());
            proveedor.setContrasenia(new BCryptPasswordEncoder().encode(contrasenia));
            proveedor.setActivo(true);
            Profesion profesion1 = profesionServicio.buscarProfesion(profesion);
            if(profesion1 != null ){
                proveedor.setProfesion(profesion1);
            } else if(profesion1== null){
                proveedor.setProfesion(proveedor.getProfesion());
            }
//            proveedor.setCbu(cbu);
            proveedor.setCostoHora(costoXHora);
//            proveedor.setMatricula(matricula);
            if (archivo.isEmpty()) {
                Imagen imagen = proveedor.getImagen();
                proveedor.setImagen(imagen);
                proveedorRepositorio.save(proveedor);
            } else {
                String idImagen = proveedor.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                proveedor.setImagen(imagen);
                proveedorRepositorio.save(proveedor);
            }
            return proveedor;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List listarProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        proveedores = proveedorRepositorio.findAll();
        return proveedores;
    }

    public List<Proveedor> puntuacionP(Integer puntuacion) {
        List<Proveedor> puntuaciones = (List<Proveedor>) proveedorRepositorio.buscarPorPuntuacion(puntuacion);
        return puntuaciones;
    }

    public List proveedoresActivos() {
        List<Proveedor> estadosActivos = proveedorRepositorio.obtenerPerfilesActivos();
        return estadosActivos;
    }

    public List proveedoresInactivos() {
        List<Proveedor> estadosInactivos = proveedorRepositorio.obtenerPerfilesInactivos();
        return estadosInactivos;
    }

    public Proveedor getOne(String id) {
        return proveedorRepositorio.getOne(id);
    }


    private void validar(String nombre, String correo, String contrasenia, String contrasenia2, String direccion,
                         String profesion, /*Integer cbu,*/ Double costoXHora /*, String matricula*/) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El usuario no puede estar en blanco");
        }
        if (correo.isEmpty() || correo == null) {
            throw new MiException("El Correo no puede estar en blanco");
        }
        if (contrasenia.isEmpty() || contrasenia == null) {
            throw new MiException("La contraseña no puede estar vacia");

        } else if (contrasenia.length() < 6) {
            throw new MiException("La contraseña no puede ser menor de 6 caracteres");
        }
        if (!contrasenia.equals(contrasenia2)) {
            throw new MiException("La contraseña no coincide");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new MiException("Debe ingresar una direccion");
        }

        if (profesion == null) {
            throw new MiException("Debe añadir una profesion");
        }
//
//        if (cbu == null) {
//            throw new MiException("Debe un cbu para registrar su pago");
//        }

        if (costoXHora == null) {
            throw new MiException("Debe ingresar un monto base de Honorarios");
        }

//        if (matricula.isEmpty() || matricula == null) {
//            throw new MiException("Debe ingresar su matricula para continuar");
//        }
    }

    public List listarProfesiones() {

        List<Profesion> profesiones = profesionServicio.listarProfesiones();
        return profesiones;
    }

    @Transactional
    public void crearAlbum(MultipartFile archivo, String idProveedor) throws MiException {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        System.out.println( "holiwisssss");
        if (respuesta.isPresent()) {
            System.out.println("Entro todooooo");
            System.out.println(archivo.getName().toString());
            Proveedor proveedor = respuesta.get();
            List<Imagen> album = proveedor.getImagenes(); // new ArrayList<>();// (List<Imagen>) imagenServicio.agregarImagen(archivo);
            Imagen imagen = imagenServicio.guardar(archivo);
            album.add(imagen);

            //proveedor.getImagenes().add(imagen);
            proveedor.setImagenes(album);
            proveedorRepositorio.save(proveedor);
            System.out.println( "salio");
        }
    }

    public void calificacion(Integer puntuacion, String idProveedor) {

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            if (puntuacion > 0 && puntuacion < 6) {
                proveedor.setPuntuacion(puntuacion);
                proveedorRepositorio.save(proveedor);
            }
        }
    }

    @Transactional
    public void tareasEnCurso(Contrato contrato) {
        if (contrato!=null){
        Proveedor proveedor = getOne(contrato.getProveedor().getId());
        Cliente cliente = clienteServicio.getOne(contrato.getCliente().getId());
            proveedor.getContratosEnCursoP().add(contrato);
            if (!proveedor.getClientes().contains(contrato.getCliente())) {
                proveedor.getClientes().add(cliente);
                proveedorRepositorio.save(proveedor);
            } else {
        proveedorRepositorio.save(proveedor);}
        }
    }
    @Transactional(readOnly = true)
    public List<Contrato> listarTrabajosEnCurso(String id){
       Optional<Proveedor> respuesta= proveedorRepositorio.findById(id);
if(respuesta.isPresent()){
    Proveedor proveedor= respuesta.get();
        List<Contrato> trabajos= proveedor.getContratosEnCursoP();
        return trabajos;
 } return null;
    }

    /*public void tareasTerminadas(Contrato contrato, String idProveedor) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(idProveedor);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            List<Contrato> lista = proveedor.getContratosEnCurso();

            if (lista.contains(contrato)) {
                lista.remove(contrato);
                List<Contrato> trabajoFinalizado = proveedor.getContratoFinalizado();
                trabajoFinalizado.add(contrato);
                proveedor.setContratosEnCurso(lista);
                proveedor.setContratoFinalizado(trabajoFinalizado);
                proveedorRepositorio.save(proveedor);
            }

        }

    }

    public void calificard(Contrato contrato ){
        Optional<Contrato> respuesta = contratoRepositorio.findById(contrato.getIdPedido());
        if(respuesta.isPresent()){
            Contrato contrato1= respuesta.get();
            Proveedor proveedor= getOne(contrato1.getProveedor().getId());
          //  Cometario.crearcomentario

        }
        tareasTerminadas(contrato, contrato.getProveedor().getId());
    }*/

    @Transactional
    public void aceptarTrabajo(Cliente cliente, Proveedor proveedor) {
        // Contrato contrato = contratoServicio.obtenerContrato(proveedor, cliente);
        Contrato contrato= contratoServicio.obtenerContratoactivo(proveedor,cliente);
        clienteServicio.agregarContrato(contrato);
    }

   /* @Transactional
    public void declinarTrabajo(Cliente cliente, Proveedor proveedor){
       // Contrato contrato= contratoServicio.obtenerContrato(proveedor, cliente);
          proveedor.getContratosEnCurso().remove(contrato);
          contratoServicio.eliminarContrato(proveedor, cliente);
        proveedorRepositorio.save(proveedor);
    }*/

    @Transactional
    public void declinarTrabajo(Contrato contrato) {

        Proveedor proveedor = proveedorRepositorio.findById(contrato.getProveedor().getId()).orElse(null);
        if (proveedor != null) {
            // Acceder a la colección dentro de la transacción
            proveedor.getContratosEnCursoP().remove(contrato);
            contratoServicio.eliminarContrato(contrato);
            clienteServicio.declinarTrabajoCliente(contrato);
            proveedorRepositorio.save(proveedor);
        }
    }

    @Transactional
    public void terminadoTrabajo(Contrato contrato) {
        Proveedor proveedor = proveedorRepositorio.findById(contrato.getProveedor().getId()).orElse(null);
        if (proveedor!= null) {
            proveedor.getContratosEnCursoP().remove(contrato);
            contrato= contratoServicio.eliminarContrato(contrato);
            proveedor.getContratoFinalizadoP().add(contrato);
            clienteServicio.finalizarContrato(contrato);
            proveedorRepositorio.save(proveedor);
        }

    }

   /* @Transactional
    public void terminadoTrabajo(Cliente cliente, Proveedor proveedor) {
        Contrato contrato = contratoServicio.obtenerContrato(proveedor, cliente);
        proveedor.getContratoFinalizado().add(contrato);
        proveedor.getContratosEnCurso().remove(contrato);
        // contratoServicio.eliminarContrato(proveedor, cliente);
        clienteServicio.finalizarContrato(cliente, contrato);

        proveedorRepositorio.save(proveedor);


    }
*/
   public List<Proveedor> buscarProveedores(String nombre, String direccion, String profesion) {
       List<Proveedor> resultadosNombre = proveedorRepositorio.findByNombreContainingIgnoreCase(nombre);
       List<Proveedor> resultadosUbicacion = proveedorRepositorio.findByDireccionContainingIgnoreCase(direccion);
       Profesion profesionn= profesionRepositorio.buscarProfesion(profesion);
       List<Proveedor> resultadosProfesion = proveedorRepositorio.findByProfesion(profesionn);

       // Combinar resultados (puedes necesitar manejar duplicados según tus necesidades)
       Set<Proveedor> resultadoFinal = new HashSet<>();
       resultadoFinal.addAll(resultadosNombre);
       resultadoFinal.addAll(resultadosUbicacion);
       resultadoFinal.addAll(resultadosProfesion);

      // return new ArrayList<>(resultadoFinal);
      List<Proveedor> resultadoFinalList= new ArrayList<>(resultadoFinal);
       resultadoFinalList.sort(Comparator.comparing(Proveedor::getNombre));
       return resultadoFinalList;
   }

    public List<Proveedor> obtenerProveedoresOrdenados(String opcion) {
        // Obtener todos los proveedores ordenados por nombre ascendentemente
        if (opcion.equals("ascendente")) {

            return proveedorRepositorio.findAllByOrderByNombreAsc();

        } else {
           return proveedorRepositorio.findAllByOrderByNombreDesc();
        }
    }

   public List<Map<String, String>> retornarAlbum(String id){

       Optional <Proveedor>respuesta = proveedorRepositorio.findById(id);
       if (respuesta.isPresent()){
           Proveedor proveedor= respuesta.get();
           List<Imagen> imagenes = proveedor.getImagenes();
           List<Map<String, String>> imagenesInfo = new ArrayList<>();

           for (Imagen imagen : imagenes) {
               Map<String, String> imageData = new HashMap<>();
               byte[] imagenContenido = imagen.getContenido();
               String imagenBase64 = Base64.getEncoder().encodeToString(imagenContenido);
               imageData.put("base64", imagenBase64);
               imageData.put("mime", imagen.getMime());
               imageData.put("nombre", imagen.getNombre());
               imagenesInfo.add(imageData);
           }
            return imagenesInfo;
       }

       return null;
   }
}




