package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.servicios.UsuarioServicio;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @GetMapping("/modificar-usuario-datos/{idUsuarioModif}")
    public String datosUsuario(ModelMap model, @PathVariable String idUsuarioModif) {
        Usuario usuario = usuarioServicio.buscarPorId(idUsuarioModif);
        model.addAttribute("usuarioModif", usuario);
        return "modif-usuario.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");//recupero el usuario de la seccion 
        if (login == null || !login.getId().equals(id)) { //si es null en la seccion no hay ningun usuario
            return "redirect:/inicio";
        }
        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            model.addAttribute("perfil", usuario);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }

        return "perfil";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/actualizar-perfil")
    public String registrar(ModelMap modelo, HttpSession session, MultipartFile archivo,
            @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String dni, @RequestParam String telefono, @RequestParam String email,
            @RequestParam String direccion, @RequestParam Date fechaDeNacimiento,
            @RequestParam String clave, @RequestParam String clave2) {
        Usuario usuario = null;
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");//recupero el usuario de la seccion 
            if (login == null || !login.getId().equals(id)) { //si es null en la seccion no hay ningun usuario
                return "redirect:/inicio";
            }

            usuario = usuarioServicio.buscarPorId(id);
            usuarioServicio.modificar(archivo, id, nombre, apellido, fechaDeNacimiento, dni, telefono, email, direccion, clave2, clave2);
            session.setAttribute("usuariosession", usuario);
            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", usuario);

            return "perfil";
        }
    }

}
