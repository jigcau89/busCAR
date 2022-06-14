package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.entidades.Transaccion;
import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.FormaDePago;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.servicios.TransaccionServicio;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @GetMapping("/pago")
    public String pago(ModelMap modelo, @RequestParam Double precio) {
        if (((precio * 20) / 100) > 100000d) {
            precio = 100000d;
        } else if (((precio * 20) / 100) < 15000d) {
            precio = 15000d;
        } else {
            precio = ((precio * 20) / 100); 
        }
        modelo.put("reserva", precio.intValue());
        return null;
    }

    @PostMapping("/pago")
    public String pago(ModelMap modelo, @RequestParam Double precio, @RequestParam String idUsuario, @RequestParam String idVehiculo, @RequestParam FormaDePago metodoPago, @RequestParam Double monto) {
        if (((precio * 20) / 100) > 100000d) {
            precio = 100000d;
        } else if (((precio * 20) / 100) < 15000d) {
            precio = 15000d;
        } else {
            precio = ((precio * 20) / 100); 
        }
        modelo.put("reserva", precio.intValue());
        try {
            servicioTransaccion.venta(idUsuario, idVehiculo, metodoPago, monto);
            modelo.put("exito", "");
        } catch (ErrorServicio ex) {
            modelo.put("error", "");
        }
        return null;
    }

    @GetMapping("/gestion")
    public String gestion(ModelMap modelo) {
        List<Transaccion> transacciones = servicioTransaccion.buscarPorAlta(Boolean.TRUE);
        modelo.put("transacciones", transacciones);
        return "gestion_transaccion";
    }

    @GetMapping("/editar")
    public String editar(ModelMap modelo, @RequestParam String id) {
        Transaccion transaccion = servicioTransaccion.buscarPorId(id);
        modelo.put("transaccion", transaccion);
        return "transaccion/transaccion_editar";
    }

    @PostMapping("/editar") /// no aparecen los mensajes pero funciona
    public String editar(ModelMap modelo, @RequestParam String id, @RequestParam Date fechaTransaccion, Double monto, FormaDePago formaDePago, Usuario usuario, Vehiculo vehiculo) {
        try {
            servicioTransaccion.modificar(id, fechaTransaccion, monto, formaDePago, usuario, vehiculo);
            modelo.put("exito", "El autor se modificó con exito!");
        } catch (ErrorServicio e) {
            modelo.put("error", "No se pudo modificar correctamente...");
        }
        Transaccion transaccion = servicioTransaccion.buscarPorId(id);
        modelo.put("autor", transaccion);
        return "transaccion/transaccion_editar";
    }

    @GetMapping("/alta")
    public String alta(ModelMap modelo, @RequestParam String id) throws ErrorServicio {
        try {
            servicioTransaccion.habilitar(id);
        } catch (ErrorServicio e) {
            e.printStackTrace();
        }
        return "redirect:/transaccion/gestion";
    }

    @GetMapping("/baja")
    public String baja(ModelMap modelo, @RequestParam String id) {
        try {
            servicioTransaccion.deshabilitar(id);
        } catch (ErrorServicio e) {
            e.printStackTrace();
        }
        return "redirect:/transaccion/gestion";
    }
}
