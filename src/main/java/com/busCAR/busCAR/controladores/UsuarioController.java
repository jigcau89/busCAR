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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, /*@RequestParam String id,*/ ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");//recupero el usuario de la seccion 
//        if (login == null || !login.getId().equals(id)) { //si es null en la seccion no hay ningun usuario
//           // return "redirect:/index";
//           return "exito";
//        }
        //  try {
        //    Usuario usuario = usuarioServicio.buscarPorId(id);
        //    model.addAttribute("perfil", usuario);//perfil->usuarioModif
        //     } catch (ErrorServicio e) {
        //    model.addAttribute("error", e.getMessage());
        //   }
        model.put("perfil", login);
        model.put("nombre", login.getNombre());
        model.put("apellido", login.getApellido());
        model.put("fechaDeNacimiento", login.getFechaDeNacimiento());
        model.put("dni", login.getDni());
        model.put("email", login.getEmail());
        model.put("telefono", login.getTelefono());
        model.put("direccion", login.getDireccion());
        model.put("clave", login.getClave());
        model.put("clave2", login.getClave());

        return "modDatosUsuario";
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @PostMapping("/actualizar-perfil")
    public String registrar(ModelMap modelo, HttpSession session, MultipartFile archivo,
            @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String dni, @RequestParam String telefono, @RequestParam String email,
            @RequestParam String direccion, @RequestParam Date fechaDeNacimiento) {
        Usuario usuario = null;
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");//recupero el usuario de la seccion 
            if (login == null || !login.getId().equals(id)) { //si es null en la seccion no hay ningun usuario
               // return "redirect:/inicio";
               return "index";
            }

            usuario = usuarioServicio.buscarPorId(id);

            usuarioServicio.modificar(archivo, id, nombre, apellido, dni, telefono, direccion, fechaDeNacimiento, usuario.getClave(), usuario.getClave());
            session.setAttribute("usuariosession", usuario);
            //return "redirect:/inicio";
            return "index";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", usuario);

            return "modDatosUsuario";
        }
    }

    @GetMapping("/editar-clave")
    public String editarClave(HttpSession session, ModelMap modelo) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
//        modelo.put("NombreCompleto", login.getNombre());
//          modelo.put("CorreoElectronico", login.getEmail());
//        modelo.put("DocumentoDeIdentidad", login.getDNI());
        return "modClaveUsuario";
    }

    @PostMapping("/edita-clave")
    public String editaClave(HttpSession session, ModelMap modelo, @RequestParam String clave, @RequestParam String clave2) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        try {
            usuarioServicio.editarClave(login.getId(), clave, clave2);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "modClaveUsuario";
        }
        modelo.put("mensaje", "Datos modificados con éxito");
        return "modClaveUsuario";
    }

}
