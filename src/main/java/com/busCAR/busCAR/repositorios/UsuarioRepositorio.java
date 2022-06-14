package com.busCAR.busCAR.repositorios;

import com.busCAR.busCAR.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT a FROM Usuario a WHERE a.email LIKE :email AND a.activo = true")
    public Usuario buscarPorMail(@Param("email") String email);
    
    @Query("SELECT n FROM Usuario n WHERE n.nombre = :nombre")
    Usuario buscarPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT u FROM Usuario u WHERE d.dni = :dni")
    Usuario buscarPorDNI(@Param("dno") String dni);
    
    // Devuelve una Lista con Usuarios dados de alta.
    @Query("SELECT u FROM Usuario u WHERE u.baja IS null")
    public List<Usuario> buscarActivos();

    // Devuelve una Lista con Usuarios dados de baja.
    @Query("SELECT u FROM Usuario u WHERE u.baja IS NOT null")
    public List<Usuario> buscarInactivos();

}
