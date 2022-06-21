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
import com.busCAR.busCAR.servicios.UsuarioServicio;
import com.busCAR.busCAR.servicios.VehiculoServicio;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
@RequestMapping("/vehiculo")
public class VehiculoController {

    @Autowired
    private VehiculoServicio serviciovehiculo;
    @Autowired
    private UsuarioServicio usuarioservicio;

    @GetMapping("/producto")
    public String producto() {
        return "Producto";
    }

    /*ENUMS*/
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @GetMapping("/registro")
    public String colorear(ModelMap vista, HttpSession session) {
        
       
        
        vista.addAttribute("Colores", Color.values());
        vista.addAttribute("Tdc", TipoDeCombustible.values());
        vista.addAttribute("Marcas", Marca.values());
        vista.addAttribute("Tdv", TipoDeVehiculo.values());
        vista.addAttribute("id");
        return "Registro_auto";
    }

    /*ABMS*/
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @GetMapping("/modificar_vehiculo")
    public String editarPerfil(@RequestParam(required = false) String id, ModelMap vista, HttpSession session)throws ErrorServicio {
        Vehiculo vehiculo ;
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        
        id = login.getId();
        vehiculo = serviciovehiculo.buscarPorId(id) ;

        vista.put("perfil", vehiculo);
        vista.addAttribute("id", id);
        vista.addAttribute("modelo", vehiculo.getModelo());
        vista.addAttribute("patente", vehiculo.getPatente());
        vista.addAttribute("anio", vehiculo.getAnioFabricacion());
        vista.addAttribute("kilometraje", vehiculo.getKilometraje());
        vista.addAttribute("precio", vehiculo.getPrecio());
        vista.addAttribute("descripcion", vehiculo.getDescripcion());
        vista.addAttribute("Colores", Color.values());
        vista.addAttribute("Tdc", TipoDeCombustible.values());
        vista.addAttribute("Marcas", Marca.values());
        vista.addAttribute("Tdv", TipoDeVehiculo.values());

        return "Mis-Datos_vehiculo";

    }
  
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @GetMapping("/actualizar_vehiculo")
    public String actualizar(HttpSession session,ModelMap vista, @RequestParam(required = false) String id, MultipartFile archivo, String patente, String modelo, String marca, Integer anio,
            Color color, Double precio, Boolean nuevo, String kilometraje, TipoDeCombustible tdc, String descripcion,
            Boolean alta, TipoDeVehiculo tdv) throws ErrorServicio {
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
         id = "4d653c93-f3ee-418c-be87-589dbbcefc76";

      
      
        try {

            serviciovehiculo.modificar(id, archivo, patente, modelo, marca, anio, color, precio, nuevo, kilometraje, tdc, descripcion, true, tdv);
            return "redirect:/";
        } catch (ErrorServicio e) {
            e.printStackTrace();

            vista.put("error", e.getMessage());
            vista.addAttribute("Colores", Color.values());
            vista.addAttribute("Tdc", TipoDeCombustible.values());
            vista.addAttribute("Marcas", Marca.values());
            vista.addAttribute("Tdv", TipoDeVehiculo.values());
            return "Registro_auto";

        }

    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @PostMapping("/registro")
    public String registro(HttpSession session,ModelMap model, @RequestParam MultipartFile archivo, @RequestParam String patente, @RequestParam String modelo, @RequestParam String marca, @RequestParam Integer anio,
            @RequestParam Color color, @RequestParam Double precio, @RequestParam Boolean nuevo, @RequestParam String kilometraje, @RequestParam TipoDeCombustible tdc, @RequestParam String descripcion,
            @RequestParam TipoDeVehiculo tdv, Usuario us) throws ErrorServicio {
        
        
         Usuario login = (Usuario) session.getAttribute("usuariosession");
        
        try {
 
            serviciovehiculo.guardar(archivo, patente, modelo, marca, anio, color, precio, nuevo, kilometraje, tdc, descripcion, true, tdv, login);
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
