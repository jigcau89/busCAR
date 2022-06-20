package com.busCAR.busCAR.entidades;

import com.busCAR.busCAR.enumeraciones.Color;
import com.busCAR.busCAR.enumeraciones.TipoDeCombustible;
import com.busCAR.busCAR.enumeraciones.TipoDeVehiculo;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Vehiculo {

    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    private String id;
    private String patente;
    private String modelo;
    private String marca;
    private Integer anioFabricacion;
    private Color color;
    private Double precio;
    private Boolean nuevo;
    private String kilometraje;
    private TipoDeCombustible tipoDeCombustible;
    private String descripcion;
    private Boolean alta;
    private TipoDeVehiculo tipoDeVehiculo;
    @ManyToOne
    private Foto fotos;
    @ManyToOne
    private Usuario usuario;

    public Vehiculo() {
    }

    public Vehiculo(String id, String patente, String modelo, String marca, Integer anioFabricacion, Color color, Double precio, Boolean nuevo, String kilometraje, TipoDeCombustible tipoDeCombustible, String descripcion, Boolean alta, TipoDeVehiculo tipoDeVehiculo, Foto fotos, Usuario usuario) {
        this.id = id;
        this.patente = patente;
        this.modelo = modelo;
        this.marca = marca;
        this.anioFabricacion = anioFabricacion;
        this.color = color;
        this.precio = precio;
        this.nuevo = nuevo;
        this.kilometraje = kilometraje;
        this.tipoDeCombustible = tipoDeCombustible;
        this.descripcion = descripcion;
        this.alta = alta;
        this.tipoDeVehiculo = tipoDeVehiculo;
        this.fotos = fotos;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAnioFabricacion() {
        return anioFabricacion;
    }

    public void setAnioFabricacion(Integer anioFabricacion) {
        this.anioFabricacion = anioFabricacion;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public TipoDeCombustible getTipoDeCombustible() {
        return tipoDeCombustible;
    }

    public void setTipoDeCombustible(TipoDeCombustible tipoDeCombustible) {
        this.tipoDeCombustible = tipoDeCombustible;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public TipoDeVehiculo getTipoDeVehiculo() {
        return tipoDeVehiculo;
    }

    public void setTipoDeVehiculo(TipoDeVehiculo tipoDeVehiculo) {
        this.tipoDeVehiculo = tipoDeVehiculo;
    }

    public Foto getFotos() {
        return fotos;
    }

    public void setFotos(Foto fotos) {
        this.fotos = fotos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Vehiculo{" + "id=" + id + ", patente=" + patente + ", modelo=" + modelo + ", marca=" + marca + ", anioFabricacion=" + anioFabricacion + ", color=" + color + ", precio=" + precio + ", nuevo=" + nuevo + ", kilometraje=" + kilometraje + ", tipoDeCombustible=" + tipoDeCombustible + ", descripcion=" + descripcion + ", alta=" + alta + ", tipoDeVehiculo=" + tipoDeVehiculo + ", fotos=" + fotos + ", usuario=" + usuario + '}';
    }
    
    

   

}
