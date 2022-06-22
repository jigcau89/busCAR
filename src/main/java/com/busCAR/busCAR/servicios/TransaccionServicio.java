package com.busCAR.busCAR.servicios;

import com.busCAR.busCAR.entidades.Transaccion;
import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.FormaDePago;
import com.busCAR.busCAR.enumeraciones.TipoDeVehiculo;
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
        if (vehiculo == null) {
            throw new ErrorServicio("El vehiculo no existe.");
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
    }

    public void validarDatosUsuario(Usuario usuario, String nombre, String dni, String cuil, String telefono, String email, String direccion) throws ErrorServicio {
        String nombreCompleto = usuario.getNombre() + ' ' + usuario.getApellido();
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El campo de nombre no puede estar vacío.");
        } else if (!nombre.equalsIgnoreCase(nombreCompleto)) {
            throw new ErrorServicio("El nombre del usuario no coincide con el ingresado.");
        }
        if (dni == null || dni.trim().isEmpty()) {
            throw new ErrorServicio("El campo de DNI no puede estar vacío.");
        } else if (!dni.equalsIgnoreCase(usuario.getDni())) {
            throw new ErrorServicio("El DNI del usuario no coincide con el ingresado.");
        }
        if (cuil == null || cuil.trim().isEmpty()) {
            throw new ErrorServicio("El campo de cuil no puede estar vacío.");
        } else if (!cuil.contains(dni) || cuil.length() != 11) {
            throw new ErrorServicio("El cuil no es válido.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ErrorServicio("El campo de telefono no puede estar vacío.");
        } else if (!telefono.equalsIgnoreCase(usuario.getTelefono())) {
            throw new ErrorServicio("El telefono del usuario no coincide con el ingresado.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new ErrorServicio("El campo de direccion no puede estar vacío.");
        } else if (!direccion.equalsIgnoreCase(usuario.getDireccion())) {
            throw new ErrorServicio("La dirección del usuario no coincide con la ingresada.");
        }
    }

    public void validarDatosTransaccion(Usuario usuario, String nombre, String dni, String telefono, String email, String direccion, String cuil,
            String banco, String cbu, String alias) throws ErrorServicio {
        validarDatosUsuario(usuario, nombre, dni, cuil, telefono, email, direccion);

        if (banco == null || banco.trim().isEmpty()) {
            throw new ErrorServicio("El campo de banco no puede estar vacío.");
        }
        if (cbu == null || cbu.trim().isEmpty()) {
            throw new ErrorServicio("El campo de cbu no puede estar vacío.");
        } else if (cbu.length() < 6 && cbu.length() > 22) {
            throw new ErrorServicio("El cbu no es válido.");
        }
        if (alias == null || alias.trim().isEmpty()) {
            throw new ErrorServicio("El campo de alias no puede estar vacío.");
        }
    }

    public void validarDatosTarjeta(Usuario usuario, String nombre, String dni, String telefono, String email, String direccion, String cuil,
            String numeroTarjeta, String vencimiento, String codigoSeguridad) throws ErrorServicio {
        validarDatosUsuario(usuario, nombre, dni, cuil, telefono, email, direccion);
        
//        vencimiento = vencimiento.replaceAll("-", "/");
//        Date fechaVenc = new Date(vencimiento);
        if (numeroTarjeta == null || numeroTarjeta.trim().isEmpty()) {
            throw new ErrorServicio("El campo del número de la tarjeta no puede estar vacío.");
        } else if (numeroTarjeta.length() != 16) {
            throw new ErrorServicio("El número de la tarjeta debe tener 16 dígitos.");
        }
        if (vencimiento == null || vencimiento.trim().isEmpty()) {
            throw new ErrorServicio("El campo de vencimiento no puede estar vacío.");
        } /*else if (fechaVenc.before(new Date())) {
            throw new ErrorServicio("La tarjeta ya caducó.");
        }*/
        if (codigoSeguridad == null || codigoSeguridad.trim().isEmpty()) {
            throw new ErrorServicio("El campo del código de seguridad no puede estar vacío.");
        } else if (codigoSeguridad.length() != 3) {
            throw new ErrorServicio("El código de seguridad debe tener 3 dígitos.");
        }
    }
    
    public void validarDatosEfectivo(Usuario usuario, String nombre, String dni, String telefono, String email, String direccion, String cuil,
            String puntoPago) throws ErrorServicio {
        validarDatosUsuario(usuario, nombre, dni, cuil, telefono, email, direccion);
        
        if (puntoPago == null || puntoPago.trim().isEmpty()) {
            throw new ErrorServicio("El campo de punto de pago no puede estar vacío.");
        } else if (!puntoPago.equalsIgnoreCase("RapiPago") && !puntoPago.equalsIgnoreCase("PagoFacil")) {
            throw new ErrorServicio("No aceptamos el punto de pago elegido.");
        }
    }
    
    public void validarDatosCripto(Usuario usuario, String nombre, String dni, String telefono, String email, String direccion, String cuil,
            String direccionWallet, String red, String moneda) throws ErrorServicio {
        validarDatosUsuario(usuario, nombre, dni, cuil, telefono, email, direccion);
        
        if (direccionWallet == null || direccionWallet.trim().isEmpty()) {
            throw new ErrorServicio("El campo de direccion de wallet no puede estar vacío.");
        }
        if (red == null || red.trim().isEmpty()) {
            throw new ErrorServicio("El campo de red no puede estar vacío.");
        }
        if (moneda == null || moneda.trim().isEmpty()) {
            throw new ErrorServicio("El campo de moneda no puede estar vacío.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void guardar(Double monto, FormaDePago formaDePago, Usuario usuario, Vehiculo vehiculo) throws ErrorServicio {
        try {
            Date fechaTransaccion = new Date();
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
    public void borrar(String id) throws ErrorServicio {
        try {
            Optional<Transaccion> respuesta = repositorioTransaccion.findById(id);
            if (respuesta.isPresent()) {
                Transaccion transaccion = respuesta.get();
                repositorioTransaccion.delete(transaccion);
            } else {
                throw new ErrorServicio("La transacción no existe.");
            }
        } catch (ErrorServicio e) {
            e.printStackTrace();
            throw new ErrorServicio("No se pudo eliminar la transacción");
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

//    @Transactional(readOnly = true)
//    public List<Vehiculo> buscarRelacionados(TipoDeVehiculo tipoVehiculo, String idVehiculo) {
//        return repositorioTransaccion.buscarRelacionados(tipoVehiculo, idVehiculo).subList(0, 3);
//    }
}
