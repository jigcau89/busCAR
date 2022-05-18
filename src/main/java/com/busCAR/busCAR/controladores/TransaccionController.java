package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.servicios.TransaccionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transaccion")
public class TransaccionController {

    @Autowired
    private TransaccionServicio servicioTransaccion;
}
