package com.busCAR.busCAR.servicios;

import com.busCAR.busCAR.entidades.Transaccion;
import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.FormaDePago;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.repositorios.TransaccionRepositorio;
import com.busCAR.busCAR.repositorios.UsuarioRepositorio;
import com.busCAR.busCAR.repositorios.VehiculoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransaccionServicio {

    @Autowired
    private TransaccionRepositorio repositorioTransaccion;

    @Autowired
    private UsuarioRepositorio repositorioUsuario;

    @Autowired
    private VehiculoRepositorio repositorioVehiculo;

    public void validar(Date fechaTransaccion, Double monto, FormaDePago formaDePago, Usuario usuario, Vehiculo vehiculo) throws ErrorServicio {
        Date fechaActual = new Date();
        if (usuario == null) {
            throw new ErrorServicio("El usuario no existe.");
        }
        if (fechaTransaccion == null || fechaTransaccion.before(fechaActual)) {
            throw new ErrorServicio("La fecha de transacción no es válida.");
        }
        if (monto == null || monto < 0) {
            throw new ErrorServicio("El monto no es válido.");
        }
        if (formaDePago == null || formaDePago.toString().trim().isEmpty()) {
            throw new ErrorServicio("La forma de pago no es válida");
        }
        if (vehiculo == null) {
            throw new ErrorServicio("El vehiculo no existe.");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void guardar(Date fechaTransaccion, Double monto, FormaDePago formaDePago, Usuario usuario, Vehiculo vehiculo) throws ErrorServicio {
        try {
            validar(fechaTransaccion, monto, formaDePago, usuario, vehiculo);
            Transaccion transaccion = new Transaccion();
            transaccion.setFechaTransaccion(fechaTransaccion);
            transaccion.setMonto(monto);
            transaccion.setFormaDePago(formaDePago);
            transaccion.setUsuario(usuario);
            transaccion.setVehiculo(vehiculo);
            transaccion.setAlta(Boolean.TRUE);
            repositorioTransaccion.save(transaccion);
        } catch (ErrorServicio e) {
            e.printStackTrace();
            throw new ErrorServicio(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void modificar(String id, Date fechaTransaccion, Double monto, FormaDePago formaDePago, Usuario usuario, Vehiculo vehiculo) throws ErrorServicio {
        validar(fechaTransaccion, monto, formaDePago, usuario, vehiculo);
        Optional<Transaccion> respuesta = repositorioTransaccion.findById(id);
        if (respuesta.isPresent()) {
            Transaccion transaccion = respuesta.get();
            transaccion.setFechaTransaccion(fechaTransaccion);
            transaccion.setMonto(monto);
            transaccion.setFormaDePago(formaDePago);
            transaccion.setUsuario(usuario);
            transaccion.setVehiculo(vehiculo);
            repositorioTransaccion.save(transaccion);
        } else {
            throw new ErrorServicio("La transacción no existe.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void borrar(String id) {
        try {
            Optional<Transaccion> respuesta = repositorioTransaccion.findById(id);
            if (respuesta.isPresent()) {
                Transaccion transaccion = respuesta.get();
                repositorioTransaccion.delete(transaccion);
            } else {
                throw new ErrorServicio("La transacción no existe.");
            }
        } catch (Exception e) {
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Transaccion> respuesta = repositorioTransaccion.findById(id);

        if (respuesta.isPresent()) {
            Transaccion transaccion = respuesta.get();
            transaccion.setAlta(Boolean.FALSE);
            repositorioTransaccion.save(transaccion);
        } else {
            throw new ErrorServicio("La transaccion no existe.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void habilitar(String id) throws ErrorServicio {
        Optional<Transaccion> respuesta = repositorioTransaccion.findById(id);

        if (respuesta.isPresent()) {
            Transaccion transaccion = respuesta.get();
            transaccion.setAlta(Boolean.TRUE);
            repositorioTransaccion.save(transaccion);
        } else {
            throw new ErrorServicio("La transaccion no existe.");
        }
    }

    @Transactional(readOnly = true)
    public Transaccion buscarPorId(String id) {
        Optional<Transaccion> transaccion = repositorioTransaccion.findById(id);
        return transaccion.get();
    }

    @Transactional(readOnly = true)
    public List<Transaccion> buscarTodos() {
        return repositorioTransaccion.findAll();
    }

    @Transactional(readOnly = true)
    public List<Transaccion> buscarPorAlta(Boolean alta) {
        return repositorioTransaccion.buscarPorAlta(alta);
    }

    @Transactional(readOnly = true)
    public List<Transaccion> buscarPorFechaTransaccion(Date fechaTransaccion) {
        return repositorioTransaccion.buscarPorFechaTransaccion(fechaTransaccion);
    }

    @Transactional(readOnly = true)
    public List<Transaccion> buscarPorMonto(Double monto) {
        return repositorioTransaccion.buscarPorMonto(monto);
    }

    @Transactional(readOnly = true)
    public List<Transaccion> buscarPorFormaPago(FormaDePago formaDePago) {
        return repositorioTransaccion.buscarPorFormaPago(formaDePago);
    }

    @Transactional(readOnly = true)
    public List<Transaccion> buscarPorNombreUsuario(String nombre) {
        return repositorioTransaccion.buscarPorNombreUsuario(nombre);
    }

    @Transactional(readOnly = true)
    public List<Transaccion> buscarPorPatenteVehiculo(String patente) {
        return repositorioTransaccion.buscarPorPatenteVehiculo(patente);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void venta(String idUsuario, String idVehiculo, FormaDePago metodoPago, Double monto) throws ErrorServicio {
        try {
            validacionVenta(idUsuario, idVehiculo);
            Optional<Usuario> respuestaUser = repositorioUsuario.findById(idUsuario);
            Usuario usuario = respuestaUser.get();
            Optional<Vehiculo> respuestaVehiculo = repositorioVehiculo.findById(idVehiculo);
            Vehiculo vehiculo = respuestaVehiculo.get();

            Date fechaTransaccion = new Date();
            guardar(fechaTransaccion, monto, metodoPago, usuario, vehiculo);
        } catch (ErrorServicio e) {
            e.printStackTrace();
            throw new ErrorServicio(e.getMessage());
        }
    }

    public void validacionVenta(String idUsuario, String idVehiculo) throws ErrorServicio {
        Optional<Usuario> respuestaUser = repositorioUsuario.findById(idUsuario);
        Optional<Vehiculo> respuestaVehiculo = repositorioVehiculo.findById(idVehiculo);
        if (respuestaUser.isPresent()) {
            Usuario usuario = respuestaUser.get();
            if (!usuario.isActivo()) {
                throw new ErrorServicio("El usuario fue dado de baja.");
            }
        } else {
            throw new ErrorServicio("El usuario no existe.");
        }

        if (respuestaVehiculo.isPresent()) {
            Vehiculo vehiculo = respuestaVehiculo.get();
            if (!vehiculo.getAlta()) {
                throw new ErrorServicio("El vehiculo fue dado de baja o ya no hay stock.");
            }
        } else {
            throw new ErrorServicio("El vehiculo no existe.");
        }
    }
}
