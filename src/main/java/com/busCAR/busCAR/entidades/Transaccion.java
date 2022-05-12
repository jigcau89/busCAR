package com.busCAR.busCAR.entidades;

import com.busCAR.busCAR.enumeraciones.FormaDePago;
import java.util.Date;

public class Transaccion {

    private String id;

    private Date fechaTransaccion;

    private Double monto;

    private FormaDePago formaDePago;

    private Usuario usuario;

    private Vehiculo vehiculo;
}
