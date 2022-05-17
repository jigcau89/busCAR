package com.busCAR.busCAR.servicios;

import com.busCAR.busCAR.entidades.Foto;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotoRepositorio;

    //si el método se ejecuta sin lazar excepiones, entonces se hace un comit a la base de datos y se aplican todos los cambios
    //si el método laza una excepion y no es atrapada se vuelve atrás con la transacción y no se aplica nada en la base de datos
    
    @Transactional
    public Foto guardar(MultipartFile archivo) throws ErrorServicio {

        if (archivo != null && !archivo.isEmpty()) {
            try {
                Foto foto = new Foto();

                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotoRepositorio.save(foto);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public Foto actualizar(String id, MultipartFile archivo) throws ErrorServicio {
        if (archivo != null) {
            try {
                Foto foto = new Foto();

                if (id != null) {
                    Optional<Foto> respuesta = fotoRepositorio.findById(id);
                    if (respuesta.isPresent()) {
                        foto = respuesta.get();
                    }

                }

                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return fotoRepositorio.save(foto);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}