package com.busCAR.busCAR.servicios;

import com.busCAR.busCAR.entidades.Foto;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotoRepositorio;

    @Transactional
    public Foto guardar(MultipartFile archivo) throws ErrorServicio {

        if (archivo != null && !archivo.isEmpty()) {
            Foto foto = new Foto();
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            try {
                foto.setContenido(archivo.getBytes());
            } catch (IOException e) {
                Logger.getLogger(FotoServicio.class.getName()).log(Level.SEVERE, null, e);
            }
            return fotoRepositorio.save(foto);
        } else {
            return null;
        }
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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void borrar(String id) {
        Foto foto = fotoRepositorio.getById(id);
        fotoRepositorio.delete(foto);
    }

    @Transactional(readOnly = true)
    public List<Foto> buscarTodos() {
        return fotoRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Foto buscarPorId(String id) {
        return fotoRepositorio.getById(id);
    }
}
