package com.busCAR.busCAR.servicios;

import com.busCAR.busCAR.entidades.Transaccion;
import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.FormaDePago;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.repositorios.TransaccionRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransaccionServicio {

    @Autowired
    private TransaccionRepositorio transaccionRepositorio;

    public void validar(Date fechaTransaccion, Double monto, FormaDePago formaDePago, Usuario usuario, Vehiculo vehiculo) throws ErrorServicio {
        Date fechaActual = new Date();
        if (fechaTransaccion == null || fechaTransaccion.before(fechaActual)) {
            throw new ErrorServicio("La fecha de transacción no es válida.");
        }
        if (monto == null || monto < 0) {
            throw new ErrorServicio("El monto no es válido.");
        }
        if (formaDePago == null || formaDePago.toString().trim().isEmpty()) {
            throw new ErrorServicio("La forma de pago no es válida");
        }
        if (usuario == null) {
            throw new ErrorServicio("El usuario no existe.");
        }
        if (vehiculo == null) {
            throw new ErrorServicio("El vehiculo no existe.");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void guardar(Date fechaTransaccion, Double monto, FormaDePago formaDePago, Usuario usuario, Vehiculo vehiculo) {
        try {
            validar(fechaTransaccion, monto, formaDePago, usuario, vehiculo);
            Transaccion transaccion = new Transaccion();
            transaccion.setFechaTransaccion(fechaTransaccion);
            transaccion.setMonto(monto);
            transaccion.setFormaDePago(formaDePago);
            transaccion.setUsuario(usuario);
            transaccion.setVehiculo(vehiculo);
            transaccion.setAlta(Boolean.TRUE);
            transaccionRepositorio.save(transaccion);
        } catch (Exception e) {
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void modificar(String id, Date fechaTransaccion, Double monto, FormaDePago formaDePago, Usuario usuario, Vehiculo vehiculo) {
        try {
            validar(fechaTransaccion, monto, formaDePago, usuario, vehiculo);
            Optional<Transaccion> respuesta = transaccionRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Transaccion transaccion = respuesta.get();
                transaccion.setFechaTransaccion(fechaTransaccion);
                transaccion.setMonto(monto);
                transaccion.setFormaDePago(formaDePago);
                transaccion.setUsuario(usuario);
                transaccion.setVehiculo(vehiculo);
                transaccionRepositorio.save(transaccion);
            } else {
                throw new ErrorServicio("La transacción no existe.");
            }
        } catch (ErrorServicio e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
