package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.FormaDePago;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.repositorios.VehiculoRepositorio;
import com.busCAR.busCAR.servicios.TransaccionServicio;
import com.busCAR.busCAR.servicios.UsuarioServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/transaccion")
public class TransaccionController {

    @Autowired
    private TransaccionServicio servicioTransaccion;

    @Autowired
    private UsuarioServicio servicioUsuario;

    @Autowired
    private VehiculoRepositorio repositorioVehiculo;

//    @GetMapping("/producto")
//    public String producto(ModelMap modelo, @RequestParam("id_v") String idVehiculo) {
//        Optional<Vehiculo> respuesta = repositorioVehiculo.findById(idVehiculo);
//        Vehiculo vehiculo = respuesta.get();
//        modelo.put("vehiculo", vehiculo);
//
//        if (vehiculo.getNuevo()) {
//            modelo.put("estado", "Nuevo");
//        } else {
//            modelo.put("estado", "Usado");
//        }
//
//        List<Vehiculo> vehiculosRel = servicioTransaccion.buscarRelacionados(vehiculo.getTipoDeVehiculo(), vehiculo.getId());
//        modelo.put("vehiculosRel", vehiculosRel);
//        return "Producto";
//    }

    @GetMapping("/reserva")
    public String pagoReserva(ModelMap modelo, @RequestParam String id) {
        Optional<Vehiculo> respuesta = repositorioVehiculo.findById(id);
        Vehiculo vehiculo = respuesta.get();
        Double precio = vehiculo.getPrecio();

        /* Saco el 5% del precio del vehiculo, si supera los 100.000, el monto a pagar para reservar se setea en 100.000
   Si es menor a 50.000, se setea en 50.000*/
        if (((precio * 5) / 100) > 100000d) {
            precio = 100000d;
        } else if (((precio * 5) / 100) < 50000d) {
            precio = 50000d;
        } else {
            precio = ((precio * 5) / 100);
        }
        
        modelo.put("vehiculo", vehiculo);
        modelo.put("precio", precio);
        modelo.put("transferencia", FormaDePago.DEBITO);
        modelo.put("tarjeta", FormaDePago.TARJETA);
        modelo.put("rapipago", FormaDePago.EFECTIVO);
        modelo.put("cripto", FormaDePago.CRIPTOMONEDAS);
        return "reserva";
    }
    
    @GetMapping("/compra")
    public String pagoCompra(ModelMap modelo, @RequestParam String id) {
        Optional<Vehiculo> respuesta = repositorioVehiculo.findById(id);
        Vehiculo vehiculo = respuesta.get();
        
        modelo.put("vehiculo", vehiculo);
        modelo.put("precio", vehiculo.getPrecio());
        modelo.put("transferencia", FormaDePago.DEBITO);
        modelo.put("tarjeta", FormaDePago.TARJETA);
        modelo.put("rapipago", FormaDePago.EFECTIVO);
        modelo.put("cripto", FormaDePago.CRIPTOMONEDAS);
        return "reserva";
    }

    @GetMapping("/pago")
    public String pago(ModelMap modelo, @RequestParam Double precio, @RequestParam("pago") FormaDePago metodoPago, @RequestParam("id_v") String idVehiculo) {
        try {
            modelo.put("precio", precio);
            modelo.put("vehiculo", idVehiculo);
            modelo.put("metodo", metodoPago);

            switch (metodoPago) {
                case DEBITO:
                    return "datos-transferencia";
                case TARJETA:
                    return "datos-tarjeta";
                case EFECTIVO:
                    return "datos-efectivo";
                case CRIPTOMONEDAS:
                    return "datos-cripto";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "reserva";
    }

    @PostMapping("/transferencia")
    public String pagoTransferencia(ModelMap modelo, @RequestParam("id_v") String idVehiculo, @RequestParam(name = "id_u") String idUsuario,
            @RequestParam Double precio, @RequestParam String nombre, @RequestParam String dni, @RequestParam String telefono, @RequestParam String email,
            @RequestParam String direccion, @RequestParam String cuil, @RequestParam String banco, @RequestParam String cbu, @RequestParam String alias) {
        try {
            Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
            Optional<Vehiculo> vehiculo = repositorioVehiculo.findById(idVehiculo);
            servicioTransaccion.validarDatosTransaccion(usuario, nombre, dni, telefono, email, direccion, cuil, banco, cbu, alias);
            servicioTransaccion.guardar(precio, FormaDePago.DEBITO, usuario, vehiculo.get());
            return "redirect://localhost:8080/transaccion/visita";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
        }
        return "datos-transferencia";
    }

    @PostMapping("/tarjeta")
    public String pagoTarjeta(ModelMap modelo, @RequestParam("id_v") String idVehiculo, @RequestParam(name = "id_u") String idUsuario,
            @RequestParam Double precio, @RequestParam String nombre, @RequestParam String dni, @RequestParam String telefono, @RequestParam String email,
            @RequestParam String direccion, @RequestParam String cuil, @RequestParam String numeroTarjeta, @RequestParam String vencimiento,
            @RequestParam String codigoSeguridad) {
        try {
            Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
            Optional<Vehiculo> vehiculo = repositorioVehiculo.findById(idVehiculo);
            System.out.println(vencimiento);
            servicioTransaccion.validarDatosTarjeta(usuario, nombre, dni, telefono, email, direccion, cuil, numeroTarjeta, vencimiento, codigoSeguridad);
            System.out.println(vencimiento);
            servicioTransaccion.guardar(precio, FormaDePago.TARJETA, usuario, vehiculo.get());
            return "redirect://localhost:8080/transaccion/visita";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
        }
        return "datos-tarjeta";
    }

    @PostMapping("/efectivo")
    public String pagoEfectivo(ModelMap modelo, @RequestParam("id_v") String idVehiculo, @RequestParam(name = "id_u") String idUsuario,
            @RequestParam Double precio, @RequestParam String nombre, @RequestParam String dni, @RequestParam String telefono, @RequestParam String email,
            @RequestParam String direccion, @RequestParam String cuil, @RequestParam String puntoPago) {
        try {
            Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
            Optional<Vehiculo> vehiculo = repositorioVehiculo.findById(idVehiculo);
            servicioTransaccion.validarDatosEfectivo(usuario, nombre, dni, telefono, email, direccion, cuil, puntoPago);
            servicioTransaccion.guardar(precio, FormaDePago.EFECTIVO, usuario, vehiculo.get());
            return "redirect://localhost:8080/transaccion/visita";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
        }
        return "datos-efectivo";
    }

    @PostMapping("/cripto")
    public String pagoCripto(ModelMap modelo, @RequestParam("id_v") String idVehiculo, @RequestParam(name = "id_u") String idUsuario,
            @RequestParam Double precio, @RequestParam String nombre, @RequestParam String dni, @RequestParam String telefono, @RequestParam String email,
            @RequestParam String direccion, @RequestParam String cuil, @RequestParam String direccionWallet, @RequestParam String red,
            @RequestParam String moneda) {
        try {
            Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
            Optional<Vehiculo> vehiculo = repositorioVehiculo.findById(idVehiculo);
            servicioTransaccion.validarDatosCripto(usuario, nombre, dni, telefono, email, direccion, cuil, direccionWallet, red, moneda);
            servicioTransaccion.guardar(precio, FormaDePago.CRIPTOMONEDAS, usuario, vehiculo.get());
            return "redirect://localhost:8080/transaccion/visita";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
        }
        return "datos-cripto";
    }

    @GetMapping("/visita")
    public String visita() {
        return "visita";
    }
    
    @PostMapping("/visita")
    public String visita(ModelMap modelo, @RequestParam String turno, @RequestParam String contacto) {
        modelo.put("titulo", "Listo!");
        modelo.put("descripcion", "Nos comunicaremos con usted por la " + turno + ", por " + contacto + ".");
        return "exito";
    }
}
