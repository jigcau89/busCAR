package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.servicios.FotoServicio;
import com.busCAR.busCAR.servicios.UsuarioServicio;
import com.busCAR.busCAR.servicios.VehiculoServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class FotoController {

    @Autowired
    private FotoServicio servicioFoto;
    
    @Autowired
    private UsuarioServicio servicioUsuario;
    
    @Autowired
    private VehiculoServicio servicioVehiculo;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) {
        try {
            Usuario usuario = servicioUsuario.buscarPorId(id);
            if(usuario.getFoto() == null){
                throw new ErrorServicio("El usuario no tiene una foto asignada");
            }
              
            byte[] foto = usuario.getFoto().getContenido();
           
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
//    @GetMapping("/vehiculo/{id}")
//    public ResponseEntity<byte[]> fotosVehiculo(@PathVariable String id) {
//        try {
//            Vehiculo vehiculo = servicioVehiculo.buscarPorId(id);
//            if(vehiculo.getFoto() == null){
//                throw new ErrorServicio("El usuario no tiene una foto asignada");
//            }
//              
//            byte[] foto = vehiculo.getFoto().getContenido();
//           
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG);
//            
//            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
//        } catch (ErrorServicio ex) {
//            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
//             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}