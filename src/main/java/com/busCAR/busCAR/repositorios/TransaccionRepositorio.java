package com.busCAR.busCAR.repositorios;

import com.busCAR.busCAR.entidades.Transaccion;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.FormaDePago;
import com.busCAR.busCAR.enumeraciones.TipoDeVehiculo;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepositorio extends JpaRepository<Transaccion, String> {
    
    @Query("SELECT t FROM Transaccion t WHERE t.fechaTransaccion = :fecha")
    public List<Transaccion> buscarPorFechaTransaccion(@Param("fecha") Date fecha);
    
    @Query("SELECT t FROM Transaccion t WHERE t.monto = :monto")
    public List<Transaccion> buscarPorMonto(@Param("monto") Double monto);
    
    @Query("SELECT t FROM Transaccion t WHERE t.formaDePago = :formaDePago")
    public List<Transaccion> buscarPorFormaPago(@Param("formaDePago") FormaDePago formaDePago);
    
    @Query("SELECT t FROM Transaccion t WHERE t.usuario.nombre = :nombre")
    public List<Transaccion> buscarPorNombreUsuario(@Param("nombre") String nombre);
    
    @Query("SELECT t FROM Transaccion t WHERE t.vehiculo.patente = :patente")
    public List<Transaccion> buscarPorPatenteVehiculo(@Param("patente") String patente);
    
    @Query("SELECT t FROM Transaccion t WHERE t.alta = :alta")
    public List<Transaccion> buscarPorAlta(@Param("alta") Boolean alta);
    
    @Query("SELECT v FROM Vehiculo v WHERE v.id != :id_v AND v.alta = true AND v.tipoDeVehiculo LIKE :tv ORDER BY v.anioFabricacion")
    public List<Vehiculo> buscarRelacionados(@Param("tv") TipoDeVehiculo tv, @Param("id_v") String idVehiculo);
}