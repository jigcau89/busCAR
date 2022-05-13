package com.busCAR.busCAR.entidades;

import com.busCAR.busCAR.enumeraciones.FormaDePago;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Transaccion {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Temporal(TemporalType.DATE)
    private Date fechaTransaccion;
    private Double monto;
    private FormaDePago formaDePago;

    @OneToOne
    private Usuario usuario;
    @OneToOne
    private Vehiculo vehiculo;

    public Transaccion() {
    }

    public Transaccion(Date fechaTransaccion, Double monto, FormaDePago formaDePago, Usuario usuario, Vehiculo vehiculo) {
        this.id = id;
        this.fechaTransaccion = fechaTransaccion;
        this.monto = monto;
        this.formaDePago = formaDePago;
        this.usuario = usuario;
        this.vehiculo = vehiculo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public FormaDePago getFormaDePago() {
        return formaDePago;
    }

    public void setFormaDePago(FormaDePago formaDePago) {
        this.formaDePago = formaDePago;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public String toString() {
        return "Transaccion{" + "id=" + id + ", fechaTransaccion=" + fechaTransaccion + ", monto=" + monto + ", formaDePago=" + formaDePago + ", usuario=" + usuario + ", vehiculo=" + vehiculo + '}';
    }
}
