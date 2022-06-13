package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.entidades.Foto;
import com.busCAR.busCAR.entidades.Transaccion;
import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.Color;
import com.busCAR.busCAR.enumeraciones.FormaDePago;
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
    @GetMapping("/colores")
    public String colorear(ModelMap vista) {
        vista.addAttribute("Colores", Color.values());
        return "vehiculo_registro_colores";
    }

    @GetMapping("/tipo_combustible")
    public String combustear(ModelMap vista) {
        vista.addAttribute("Tipos de combustible", TipoDeCombustible.values());
        return "vehiculo_tdc";
    }

    @GetMapping("/tipo_vehiculo")
    public String tipoVehiculo(ModelMap vista) {
        vista.addAttribute("Tipo de vehículo", TipoDeVehiculo.values());
        return "vehiculo_tdv";
    }

    /*ABMS*/
    @PostMapping("/registro")
    public String registro(ModelMap model, @RequestParam MultipartFile archivo, @RequestParam String patente, @RequestParam String modelo, @RequestParam String marca, @RequestParam Integer anio,
            @RequestParam Color color, @RequestParam Double precio, @RequestParam Boolean nuevo, @RequestParam String kilometraje, @RequestParam TipoDeCombustible tdc, @RequestParam String descripcion,
            @RequestParam Boolean alta, @RequestParam TipoDeVehiculo tdv) throws ErrorServicio {
        serviciovehiculo.guardar(archivo, patente, modelo, marca, anio, color, precio, nuevo, kilometraje, tdc, descripcion, alta, tdv);
        model.put("Registro exitoso", "Vehículo guardado correctamente");
        return "vehiculo_registro";

    }

    @PostMapping("/modificar")
    public String modificar(ModelMap model, @RequestParam String id, @RequestParam MultipartFile archivo, @RequestParam String patente, @RequestParam String modelo, @RequestParam String marca, @RequestParam Integer anio,
            @RequestParam Color color, @RequestParam Double precio, @RequestParam Boolean nuevo, @RequestParam String kilometraje, @RequestParam TipoDeCombustible tdc, @RequestParam String descripcion,
            @RequestParam Boolean alta, @RequestParam TipoDeVehiculo tdv) throws ErrorServicio {
        serviciovehiculo.modificar(id, archivo, patente, modelo, marca, anio, color, precio, nuevo, kilometraje, tdc, descripcion, alta, tdv);
        model.put("modificación exitosa", "Vehículo modificado correctamente");
        return "vehiculo_modificar";
    }

    /*HABILITAR / DESHABILITAR */
    @GetMapping("/deshabilitar/{id}")
    public String baja(@PathVariable String id) {

        try {
            serviciovehiculo.deshabilitar(id);
            return "redirect:/vehiculo";
        } catch (ErrorServicio e) {
            return "redirect:/";
        }
    }

    @GetMapping("/habilitar/{id}")
    public String alta(@PathVariable String id) {

        try {
            serviciovehiculo.habilitar(id);
            return "redirect:/vehiculo";
        } catch (ErrorServicio e) {
            return "redirect:/";
        }
    }

    /*LISTAS*/
    @GetMapping("/lista")
    public String ListarTodoPorAnio(ModelMap model) {

        List<Vehiculo> listar = serviciovehiculo.ListaVehiculosOrdenadoAnio();

        model.addAttribute("Lista de vehículos por año", listar);

        return "lista-vehiculo";
    }

    @GetMapping("/lista")

    public String ListarTodoPorMarca(ModelMap model) {

        List<Vehiculo> listar = serviciovehiculo.ListaVehiculosOrdenadoMarca();

        model.addAttribute("Lista de vehículos por Marca", listar);

        return "lista-vehiculo";
    }

    public String ListarTodoPorModelo(ModelMap model) {

        List<Vehiculo> listar = serviciovehiculo.ListaVehiculosOrdenadoModelo();

        model.addAttribute("Lista de vehículos por Modelo", listar);

        return "lista-vehiculo";
    }

    public String ListarTodoPorTipo(ModelMap model) {

        List<Vehiculo> listar = serviciovehiculo.ListaVehiculosOrdenadoTipo();

        model.addAttribute("Lista de vehículos por Tipo", listar);

        return "lista-vehiculo";
    }

    public String ListarTodoPorKm(ModelMap model) {

        List<Vehiculo> listar = serviciovehiculo.ListaVehiculosOrdenadoKm();

        model.addAttribute("Lista de vehículos por Kilometraje", listar);

        return "lista-vehiculo";
        
    }
    
    
    
}
