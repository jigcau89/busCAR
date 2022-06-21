package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.servicios.UsuarioServicio;
import com.busCAR.busCAR.servicios.VehiculoServicio;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("")
public class ControladorPrincipal {

   
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    

    @GetMapping("")
    public String index() {
        
        
        return "index";
        
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session,ModelMap model) {
        
        return "inicio";
    }

    @GetMapping("/login")
    public String login(HttpSession session, Authentication usuario, ModelMap modelo, @RequestParam(required = false) String error) {
        try {
            if (usuario.getName() != null) {              
                modelo.addAttribute("session",session);
                
                return "redirect:/";
            } else {

                if (error != null && !error.isEmpty()) {
                    modelo.addAttribute("error", "La dirección de mail o la contraseña que ingresó son incorrectas.");
                }
                return "inicioSesion";
            }

        } catch (Exception e) {
            if (error != null && !error.isEmpty()) {
                modelo.addAttribute("error", "La dirección de mail o la contraseña que ingresó son incorrectas.");
            }
            return "inicioSesion";
        }
    }

    @GetMapping("/loginsuccess")
    public String loginresolver() {

        return "exito";
    }

    @GetMapping("/exito")
    public String exito() {
        return "exito";
    }

    @GetMapping("/registroUsuario")
    public String registroUsuario() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(ModelMap modelo, MultipartFile archivo, @RequestParam String nombre,
            @RequestParam String apellido, @RequestParam String dni, @RequestParam String telefono,
            @RequestParam String email, @RequestParam String direccion, @RequestParam String fechaDeNacimiento,
            @RequestParam String clave, @RequestParam String clave2) {
        try {
            fechaDeNacimiento = fechaDeNacimiento.replaceAll("-", "/");
            Date fdn = new Date(fechaDeNacimiento);
            usuarioServicio.guardar(archivo, nombre, apellido, dni, telefono, email, direccion, fdn, clave, clave2);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni", dni);
            modelo.put("telefono", telefono);
            modelo.put("email", email);
            modelo.put("direccion", direccion);
            modelo.put("fechaDeNacimiento", fechaDeNacimiento);
            modelo.put("clave", clave);
            modelo.put("clave2", clave2);
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            return "registro";
        }
        modelo.put("titulo", "Bienvenido a busCAR");
        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria.");
        return "exito";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "/contacto";
    }

    @GetMapping("/catalogoAuto")
    public String catalogoAuto() {
        return "catalogo";
    }

    @GetMapping("/catalogoMoto")
    public String catalogoMoto() {
        return "catalogo";
    }

    @GetMapping("/catalogoCamioneta")
    public String catalogoCamioneta() {
        return "catalogo";
    }
    /*Marcas*/
    @GetMapping("/catalogoChevrolet")
    public String catalogoChevrolet() {
        return "catalogo";
    }

    @GetMapping("/catalogoPeugeot")
    public String catalogoPeugeot() {
        return "catalogo";
    }

    @GetMapping("/catalogoRenault")
    public String catalogoRenault() {
        return "catalogo";
    }

    @GetMapping("/catalogoToyota")
    public String catalogoToyota() {
        return "catalogo";
    }

    @GetMapping("/catalogoVolkswagen")
    public String catalogoVolkswagen() {
        return "catalogo";
    }

    @GetMapping("/catalogoFiat")
    public String catalogoFiat() {
        return "catalogo";
    }

}
