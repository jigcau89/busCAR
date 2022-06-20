package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.servicios.FotoServicio;
import com.busCAR.busCAR.servicios.VehiculoServicio;
import com.sun.glass.ui.Window.Level;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/foto")
public class FotoController {

    @Autowired
    private FotoServicio servicioFoto;
    @Autowired
    private VehiculoServicio serviciovehiculo;

    @GetMapping("/vehiculo")
    public ResponseEntity<byte[]> fotoVehiculo(@RequestParam String id) {
        try {
            
            Vehiculo vehiculo = serviciovehiculo.buscarPorId(id);
            if(vehiculo.getFotos()== null)
            {
                throw new ErrorServicio("El vehiculo no tiene foto");
            }
            byte[] foto = vehiculo.getFotos().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        }
        catch (ErrorServicio e) {
            
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
