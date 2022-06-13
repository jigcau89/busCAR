package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.FormaDePago;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.repositorios.TransaccionRepositorio;
import com.busCAR.busCAR.repositorios.VehiculoRepositorio;
import com.busCAR.busCAR.servicios.FotoServicio;
import com.busCAR.busCAR.servicios.TransaccionServicio;
import com.busCAR.busCAR.servicios.UsuarioServicio;
import com.busCAR.busCAR.servicios.VehiculoServicio;
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
    private FotoServicio servicioFoto;

    @Autowired
    private UsuarioServicio servicioUsuario;

    @Autowired
    private VehiculoServicio servicioVehiculo;

    @Autowired
    private VehiculoRepositorio repositorioVehiculo;

    @Autowired
    private TransaccionRepositorio repositorioTransaccion;
    
    @GetMapping("/producto")
    public String producto(ModelMap modelo/*, @RequestParam String id*/) {
//        Optional<Vehiculo> respuesta = repositorioVehiculo.findById(id);

//Como todavia no tengo la vista del catalogo de auto, la busqueda está hardcodeada para agarra siempre el mismo auto
        Optional<Vehiculo> respuesta = repositorioVehiculo.findById("c827035a-ff05-44a2-92b3-caeab3fb62ee");
        Vehiculo vehiculo = respuesta.get();
        modelo.put("vehiculo", vehiculo);
        if (vehiculo.getNuevo()) {
            modelo.put("condicion", "Nuevo");
        } else {
            modelo.put("condicion", "Usado");
        }

// Necesitaria ver de donde saco las preguntas que van a ir en la vista, y ver que tan dinamico puede ser
//        String[] preguntas = new String[3];
//        preguntas[0] = "Pregunta 1";
//        preguntas[1] = "Pregunta 2";
//        preguntas[2] = "Pregunta 3";
//        modelo.put("preguntas", preguntas);
List<Vehiculo> vehiculosRel = servicioTransaccion.buscarRelacionados(vehiculo.getTipoDeVehiculo());
        modelo.put("vehiculosRel", vehiculosRel);
        return "CompraProducto";
    }

    @GetMapping("/reserva")
    public String pago(ModelMap modelo, @RequestParam String id) {
        Optional<Vehiculo> respuesta = repositorioVehiculo.findById(id);
        Vehiculo vehiculo = respuesta.get();
        Double precio = vehiculo.getPrecio();

        /* Saco el 5% del precio del vehiculo, si supera los 100.000, el monto a pagar para reservar se setea en 100.000
   Si es menor a 50.000, se setea en 50.000*/
        modelo.put("vehiculo", vehiculo);
        if (((precio * 5) / 100) > 100000d) {
            precio = 100000d;
        } else if (((precio * 5) / 100) < 50000d) {
            precio = 50000d;
        } else {
            precio = ((precio * 5) / 100);
        }
        modelo.put("precio", precio.intValue());
        modelo.put("transferencia", FormaDePago.DEBITO);
        modelo.put("tarjeta", FormaDePago.TARJETA);
        modelo.put("rapipago", FormaDePago.EFECTIVO);
        modelo.put("cripto", FormaDePago.CRIPTOMONEDAS);
        return "reserva";
    }

    @GetMapping("/pago")
    public String pago(ModelMap modelo, @RequestParam Double precio, @RequestParam FormaDePago metodoPago, @RequestParam("id_v") String idVehiculo) {
        try {
            modelo.put("monto", precio);
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

    // Como todavia no dispongo de los metodos de usuario, tengo la busqueda de un usuario hardcodeada
    @PostMapping("/transferencia")
    public String pagoTransferencia(ModelMap modelo, @RequestParam("id_v") String idVehiculo, @RequestParam(name = "id_u", required = false) String idUsuario, @RequestParam Double monto) {
        try {
//            Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
            Usuario usuario = servicioUsuario.buscarPorId("123456");
            Optional<Vehiculo> vehiculo = repositorioVehiculo.findById(idVehiculo);
            servicioTransaccion.guardar(monto, FormaDePago.DEBITO, usuario, vehiculo.get());
            modelo.put("exito", "La transacción se realizó con éxito");
        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
        }
        return "datos-transferencia";
    }

    @PostMapping("/tarjeta")
    public String pagoTarjeta(ModelMap modelo, @RequestParam("id_v") String idVehiculo, @RequestParam(name = "id_u", required = false) String idUsuario, @RequestParam Double monto) {
        try {
//            Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
            Usuario usuario = servicioUsuario.buscarPorId("123456");
            Optional<Vehiculo> vehiculo = repositorioVehiculo.findById(idVehiculo);
            servicioTransaccion.guardar(monto, FormaDePago.TARJETA, usuario, vehiculo.get());
            modelo.put("exito", "La transacción se realizó con éxito");
        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
        }
        return "datos-tarjeta";
    }

    @PostMapping("/efectivo")
    public String pagoEfectivo(ModelMap modelo, @RequestParam("id_v") String idVehiculo, @RequestParam(name = "id_u", required = false) String idUsuario, @RequestParam Double monto) {
        try {
//            Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
            Usuario usuario = servicioUsuario.buscarPorId("123456");
            Optional<Vehiculo> vehiculo = repositorioVehiculo.findById(idVehiculo);
            servicioTransaccion.guardar(monto, FormaDePago.EFECTIVO, usuario, vehiculo.get());
            modelo.put("exito", "La transacción se realizó con éxito");
        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
        }
        return "datos-efectivo";
    }

    @PostMapping("/cripto")
    public String pagoCripto(ModelMap modelo, @RequestParam("id_v") String idVehiculo, @RequestParam(name = "id_u", required = false) String idUsuario, @RequestParam Double monto) {
        try {
//            Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
            Usuario usuario = servicioUsuario.buscarPorId("123456");
            Optional<Vehiculo> vehiculo = repositorioVehiculo.findById(idVehiculo);
            servicioTransaccion.guardar(monto, FormaDePago.CRIPTOMONEDAS, usuario, vehiculo.get());
            modelo.put("exito", "La transacción se realizó con éxito");
        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage());
        }
        return "datos-cripto";
    }
}
