package com.busCAR.busCAR.servicios;

import com.busCAR.busCAR.entidades.Foto;
import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.enumeraciones.Rol;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private EmailServicio emailServicio;

    @Autowired
    private FotoServicio fotoServicio;

    @Transactional
    public void guardar(MultipartFile archivo, String id, String nombre, String apellido, String dni, String telefono, String mail, String direccion, String clave, String clave2, String rol) throws ErrorServicio {

        validar(nombre, apellido, dni, telefono, mail, direccion, clave, clave2, rol);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setEmail(mail);
        usuario.setDireccion(direccion);

        usuario.setRol(Rol.valueOf(rol));

        usuario.setClave(new BCryptPasswordEncoder().encode(clave));
        usuario.setAdmin(Boolean.TRUE);
        usuario.setFechaDeNacimiento(new Date());
        usuario.setActivo(true);

        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);

        emailServicio.enviarThread(usuario.getEmail());

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String apellido, String dni, String telefono, String mail, String direccion, String clave, String clave2, String rol) throws ErrorServicio {

        validar(nombre, apellido, dni, telefono, mail, direccion, clave, clave2, rol);
        Optional<Usuario> repuesta = usuarioRepositorio.findById(id);
        if (repuesta.isPresent()) {
            Usuario usuario = repuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setTelefono(telefono);
            usuario.setEmail(mail);
            usuario.setDireccion(direccion);

            usuario.setRol(Rol.valueOf(rol));

            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            String idFoto = null;
            if (usuario.getFoto() != null) {
                idFoto = usuario.getFoto().getId();
            }

            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            usuario.setFoto(foto);
            usuarioRepositorio.save(usuario);

        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setActivo(false);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado.");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setActivo(true);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario soliccitado");
        }
    }

    private void validar(String nombre, String apellido, String dni, String telefono, String mail, String direccion, String clave, String clave2, String rol) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty() || nombre.contains("  ")) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo. ");
        }
        if (apellido == null || apellido.isEmpty() || apellido.contains("  ")) {
            throw new ErrorServicio("El apellido del usuario no puede ser nulo. ");
        }
        if (dni == null || dni.isEmpty() || dni.contains("  ")) {
            throw new ErrorServicio("El DNI del usuario no puede ser nulo. ");
        }
        if (telefono == null || telefono.isEmpty() || telefono.contains("  ")) {
            throw new ErrorServicio("El telefono del usuario no puede ser nulo. ");
        }
        if (mail == null || mail.isEmpty() || mail.contains("  ")) {
            throw new ErrorServicio("El mail del usuario no puede ser nulo. ");
        }
        if (usuarioRepositorio.buscarPorMail(mail) != null) {
            throw new ErrorServicio("El Email ya esta en uso");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new ErrorServicio("La dirección del usuario no puede ser nulo. ");
        }
        if (clave == null || clave.isEmpty() || clave.contains("  ") || clave.length() <= 6 || clave.length() >= 12) {
            throw new ErrorServicio("La clave del usuario no puede ser nulo y tiene que tener entre 6 y 12 digitos. ");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("La clave deben ser iguales. ");
        }
        if (!Rol.ADMIN.toString().equals(rol) && !Rol.USUARIO.toString().equals(rol)) {
            throw new ErrorServicio("Debe tener un rol valido");
        }

    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepositorio.findAll();
    }

    public Usuario buscarPorId(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio(("el usuario solicitado no existe."));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            //GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariossession", usuario); //en la variable usuariosession voy a tener guardado mi objeto con todos datos del usuario que esta logeado

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }

    }

}
