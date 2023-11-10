
package com.egg.servicios.excepciones;


public class MiException extends Exception{
    
    public MiException(String msg) {
        
        super(msg);  //le paso al padre, el mensaje
    }
}
