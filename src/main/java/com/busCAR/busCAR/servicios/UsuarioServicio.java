package com.busCAR.busCAR.servicios;

import com.busCAR.busCAR.entidades.Foto;
import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.enumeraciones.Rol;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.repositorios.UsuarioRepositorio;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import org.springframework.transaction.annotation.Propagation;
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
    public void guardar(MultipartFile archivo, String nombre, String apellido,
            String dni, String telefono, String email, String direccion, 
            Date fechaDeNacimiento, String clave, String clave2/*, String rol*/) throws ErrorServicio {

        validar(nombre, apellido, dni, telefono, email, direccion, clave, clave2/*, "USUARIO"*/);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setDireccion(direccion);

        //usuario.setRol(Rol.valueOf(rol));
        usuario.setRol(Rol.USUARIO);

//        String encriptada = new BCryptPasswordEncoder().encode(clave);
//        usuario.setClave(encriptada);
        usuario.setClave(new BCryptPasswordEncoder().encode(clave));
        //usuario.setAdmin(Boolean.TRUE);
        usuario.setFechaDeNacimiento(fechaDeNacimiento);
        usuario.setActivo(true);

        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);

        emailServicio.enviarThread(usuario.getEmail());

        usuarioRepositorio.save(usuario);

    }
    
    @Transactional
    public void cargarListaVehiculos(String id, String id_v) throws ErrorServicio
    {
        Optional <Usuario> respuesta = usuarioRepositorio.findById(id);
        List <Usuario> lista;
        lista.add(id_v);
        if(respuesta.isPresent())
        {
            Usuario usuario = respuesta.get();
            usuario.setFavoritos(id_v);
        }
        
    }

    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String apellido,
             String dni, String telefono, /*String email, */String direccion,
             Date fechaDeNacimiento, String clave,
            String clave2 /*, String rol*/) throws ErrorServicio {

        Optional<Usuario> repuesta = usuarioRepositorio.findById(id);
        validar(nombre, apellido, dni, telefono, repuesta.get().getEmail(), direccion, clave, clave2/*, rol*/);

        if (repuesta.isPresent()) {
            Usuario usuario = repuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setTelefono(telefono);
//            usuario.setEmail(email);
            usuario.setDireccion(direccion);
            usuario.setFechaDeNacimiento(fechaDeNacimiento);
            //usuario.setRol(Rol.valueOf(rol)); 
            //usuario.setRol(Rol.USUARIO);

//            String encriptada = new BCryptPasswordEncoder().encode(clave);
//            usuario.setClave(encriptada);
            if (!archivo.isEmpty()) {
                String idFoto = null;
                if (usuario.getFoto() != null) {
                    idFoto = usuario.getFoto().getId();
                }
                Foto foto = fotoServicio.actualizar(idFoto, archivo);
                usuario.setFoto(foto);
            }
            usuarioRepositorio.save(usuario);

        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado");
        }
    }

    @Transactional
    public void eliminar(String id) throws ErrorServicio {
        try {
            // Usamos el repositorio para que busque el usuario cuyo id sea el pasado como parámetro.
            Usuario usuario = usuarioRepositorio.getById(id);

            if (usuario != null) {
                // Persistencia en la DB:
                usuarioRepositorio.delete(usuario);
            } else {
                throw new Exception("No existe el usuario vinculado a ese ID.");
            }
        } catch (Exception e) {
            throw new ErrorServicio(e.getMessage());
        }
    }

    @Transactional
    public void editarClave(String id, String clave, String clave2) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        validar(respuesta.get().getNombre(), respuesta.get().getApellido(), respuesta.get().getDni(), respuesta.get().getTelefono(), respuesta.get().getEmail(), respuesta.get().getDireccion(), clave, clave2);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
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

    @Transactional
    public void cambiarRol(String id) {
        Usuario usuario = usuarioRepositorio.getById(id);
        if (usuario != null) {
            // El usuario con ese id SI existe en la DB
            if (usuario.getRol().equals(Rol.USUARIO)) { // Se usa el equals pero Rol.USUARIO no va entre comillas, no es un String, es el String de un enum.
                usuario.setRol(Rol.ADMIN);
            } else if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USUARIO);
            }
        }
    }

    private void validar(String nombre, String apellido, String dni, String telefono, String email, String direccion, String clave, String clave2/*, String rol*/) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty() || nombre.contains("  ")) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo. ");
        }
        if (apellido == null || apellido.isEmpty() || apellido.contains("  ")) {
            throw new ErrorServicio("El apellido del usuario no puede ser nulo. ");
        }
//        if (fechaDeNacimiento == null || fechaDeNacimiento.toString().trim().isEmpty()) {
//            throw new ErrorServicio("La fecha de nacimiento no puede ser nulo. ");
//        }
        if (dni == null || dni.isEmpty() || dni.contains("  ")) {
            throw new ErrorServicio("El DNI del usuario no puede ser nulo. ");
        }
        if (telefono == null || telefono.isEmpty() || telefono.contains("  ")) {
            throw new ErrorServicio("El telefono del usuario no puede ser nulo. ");
        }
        if (email == null || email.trim().isEmpty() || email.contains("  ")) {
            throw new ErrorServicio("El mail del usuario no puede ser nulo. ");
        }
//        if (usuarioRepositorio.buscarPorMail(email) != null) {
//            throw new ErrorServicio("El Email ya esta en uso");
//        }
        if (direccion == null || direccion.isEmpty()) {
            throw new ErrorServicio("La dirección del usuario no puede ser nulo. ");
        }
        System.out.println(clave);
        if (clave == null || clave.trim().isEmpty() /*|| clave.length() <= 6 || clave.length() >= 12*/) {
            throw new ErrorServicio("La clave del usuario no puede ser nulo y tiene que tener entre 6 y 12 digitos. ");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("La clave deben ser iguales. ");
        }
//        if (!Rol.ADMIN.toString().equals(rol) && !Rol.USUARIO.toString().equals(rol)) {
//            throw new ErrorServicio("Debe tener un rol valido");
//        }

    }
// ------------------------------ MÉTODOS DEL REPOSITORIO ------------------------------

    /**
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepositorio.findAll();
    }

    /**
     *
     * @param id
     * @return
     * @throws ErrorServicio
     */
    public Usuario buscarPorId(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio(("el usuario solicitado no existe."));
        }
    }

    public Usuario getById(String id) {
        return usuarioRepositorio.getById(id);
    }    
    
//    public List<Usuario> buscarActivos() {
//        return usuarioRepositorio.buscarActivos();
//    }
//
//    public List<Usuario> buscarInactivos() {
//        return usuarioRepositorio.buscarInactivos();
//    }

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

            session.setAttribute("usuariosession", usuario); //en la variable usuariosession voy a tener guardado mi objeto con todos datos del usuario que esta logeado

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }

    }

}
