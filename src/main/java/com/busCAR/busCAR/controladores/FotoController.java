package com.busCAR.busCAR.controladores;

import com.busCAR.busCAR.servicios.FotoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("foto")
public class FotoController {

    @Autowired
    private FotoServicio servicioFoto;

}
