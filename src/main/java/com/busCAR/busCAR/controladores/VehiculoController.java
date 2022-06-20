package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.entidades.Foto;
import com.busCAR.busCAR.entidades.Transaccion;
import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.Color;
import com.busCAR.busCAR.enumeraciones.FormaDePago;
import com.busCAR.busCAR.enumeraciones.Marca;
import com.busCAR.busCAR.enumeraciones.TipoDeCombustible;
import com.busCAR.busCAR.enumeraciones.TipoDeVehiculo;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.servicios.TransaccionServicio;
import com.busCAR.busCAR.servicios.VehiculoServicio;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/vehiculo")
public class VehiculoController {

    @Autowired
    private VehiculoServicio serviciovehiculo;
    
    
    

    /*ENUMS*/
    @GetMapping("/registro")
    public String colorear(ModelMap vista) {
        vista.addAttribute("Colores", Color.values());
        vista.addAttribute("Tdc", TipoDeCombustible.values());
        vista.addAttribute("Marcas", Marca.values());
        vista.addAttribute("Tdv", TipoDeVehiculo.values());
        vista.addAttribute("id");
        return "Registro_auto";
    }
    
    @GetMapping("/modificar_vehiculo")
    public String modificar(ModelMap vista)
    {
        
        
        Vehiculo prueba;
        String id = "e1a398fa-501a-403b-ab00-bea886d95d49";
        prueba = serviciovehiculo.buscarPorId(id);
        
        vista.addAttribute("Colores", prueba.getColor());
        vista.addAttribute("Tdc", prueba.getTipoDeCombustible());
        vista.addAttribute("Marcas", prueba.getMarca());
        vista.addAttribute("Tdv", prueba.getTipoDeVehiculo());
        
        vista.addAttribute("modelo" , prueba.getModelo() );
        vista.addAttribute("patente" , prueba.getPatente());
        vista.addAttribute("anio" , prueba.getAnioFabricacion());
        vista.addAttribute("kilometraje" ,prueba.getKilometraje());
        vista.addAttribute("precio" , prueba.getPrecio());
        vista.addAttribute("descripcion" , prueba.getDescripcion());
        vista.addAttribute("id", id);
        
        return "Mis-datos_vehiculo";
    }
    
    
    
    
    
    @GetMapping("/producto")
    public String producto()
    {
        return "Producto";
    }

    /*ABMS*/
    @PostMapping("/registro")
    public String registro(ModelMap model, @RequestParam MultipartFile archivo, @RequestParam String patente, @RequestParam String modelo, @RequestParam String marca, @RequestParam Integer anio,
            @RequestParam Color color, @RequestParam Double precio, @RequestParam Boolean nuevo, @RequestParam String kilometraje, @RequestParam TipoDeCombustible tdc, @RequestParam String descripcion,
             @RequestParam TipoDeVehiculo tdv) throws ErrorServicio {

        try {
            serviciovehiculo.guardar(archivo, patente, modelo, marca, anio, color, precio, nuevo, kilometraje, tdc, descripcion, true, tdv);
            model.put("exito", "Vehículo guardado correctamente");
            return "Registro_auto";
        } catch (ErrorServicio e) {
            
            e.printStackTrace();
            model.put("error", "falló el registro");
            model.addAttribute("Colores", Color.values());
            model.addAttribute("Tdc", TipoDeCombustible.values());
            model.addAttribute("Marcas", Marca.values());
            model.addAttribute("Tdv", TipoDeVehiculo.values());
            return "Registro_auto";
           
            
        }

    }

}
