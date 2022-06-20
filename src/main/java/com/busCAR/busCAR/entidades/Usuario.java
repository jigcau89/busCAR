package com.busCAR.busCAR.entidades;

import com.busCAR.busCAR.enumeraciones.Rol;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @Column(nullable = false)  //no se permiten valores nulos
    private String nombre;
    
    private String apellido;

    private String dni;

    private String telefono;
    
   // @Column(unique = true) //es opcional, indica que los valores de esta columna son unicos.
    private String email; //no se puede repetir el valor

    private String direccion;

    @Temporal(TemporalType.DATE)
    //@DateTimeFormat("dd/MM/yyyy")
    private Date fechaDeNacimiento;

    @ManyToOne
    private Foto foto;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private String clave;
   
    //private List<Vehiculo> favoritos;

    private boolean activo;

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String dni, String telefono, String email, String direccion, Date fechaDeNacimiento, Foto foto, Rol rol, String clave, /*List<Vehiculo> favoritos, */boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.foto = foto;
        this.rol = rol;
        this.clave = clave;
        //this.favoritos = favoritos;
        this.activo = activo;
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido the apellido to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * @return the dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * @param dni the dni to set
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the fechaDeNacimiento
     */
    public Date getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    /**
     * @param fechaDeNacimiento the fechaDeNacimiento to set
     */
    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    /**
     * @return the foto
     */
    public Foto getFoto() {
        return foto;
    }

    /**
     * @param foto the foto to set
     */
    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    /**
     * @return the rol
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * @return the favoritos
     *//*
    public List<Vehiculo> getFavoritos() {
        return favoritos;
    }
*/
    /**
     * @param favoritos the favoritos to set
     *//*
    public void setFavoritos(List<Vehiculo> favoritos) {
        this.favoritos = favoritos;
    }
*/
    /**
     * @return the activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", dni=" + dni + ", telefono=" + telefono + ", email=" + email + ", direccion=" + direccion + ", fechaDeNacimiento=" + fechaDeNacimiento + ", foto=" + foto + ", rol=" + rol + ", clave=" + clave + ", favoritos=" /*+ favoritos*/ + ", activo=" + activo + '}';
    }
}
