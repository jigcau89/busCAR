package com.busCAR.busCAR.repositorios;

import com.busCAR.busCAR.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT a FROM Usuario a WHERE a.email LIKE :email AND a.activo = true")
    public Usuario buscarPorMail(@Param("email") String email);

}
