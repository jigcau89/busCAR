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
import org.springframework.beans.factory.annotation.Autowired;
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
        return "Registro_auto";
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
