package com.busCAR.busCAR.repositorios;

import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.Color;
import com.busCAR.busCAR.enumeraciones.TipoDeCombustible;
import com.busCAR.busCAR.enumeraciones.TipoDeVehiculo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepositorio extends JpaRepository<Vehiculo, String> {

    @Query("Select v From Vehiculo v where v.id LIKE :i")
    public Vehiculo vehiculoPorId(@Param("i") String i);
    
    /*Consultas mv*/
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true ORDER BY  v.anioFabricacion")
    public List<Vehiculo> ListaVehiculosOrdenadoAnio();

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true ORDER BY  v.marca")
    public List<Vehiculo> ListaVehiculosOrdenadoMarca();

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true ORDER BY  v.modelo")
    public List<Vehiculo> ListaVehiculosOrdenadoModelo();

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true ORDER BY  v.tipoDeVehiculo")
    public List<Vehiculo> ListaVehiculosOrdenadoTipo();

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true ORDER BY  v.kilometraje")
    public List<Vehiculo> ListaVehiculosOrdenadoKm();

    /*Todos  los vehíclos dados de alta*/
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.anioFabricacion LIKE :a ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerPorAnio(@Param("a") Integer a);

    /* Traer por año */
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.modelo LIKE :m ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerPorModelo(@Param("m") String m);/* Traer por Modelo*/


    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :mar ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerPorMarca(@Param("mar") String mar);

    /* Traer por Marca*/
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.color LIKE :c ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerPorColor(@Param("c") Color c);/* Traer por Color*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.nuevo = true ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerNuevos();

    /* Traer 0 km*/
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.nuevo = false ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerUsados();/* Traer usados*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.tipoDeCombustible LIKE :tc ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerPorTcombustible(@Param("tc") TipoDeCombustible tc);/* Traer por Tipo combustible*/


    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.tipoDeVehiculo LIKE :tv ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerPorTipov(@Param("tv") TipoDeVehiculo tv);/* Traer por Tipo de rodado*/


 /*Año de fabricacion*/
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.anioFabricacion LIKE :a AND v.marca LIKE :m ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerAnioMarca(@Param("a") Integer a, @Param("m") String m);/*Año y marca*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.anioFabricacion LIKE :a AND v.modelo LIKE :mod ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerAnioModelo(@Param("a") Integer a, @Param("mod") String mod);/*Año y modelo*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.anioFabricacion LIKE :a AND v.color LIKE :c ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerAnioColor(@Param("a") Integer a, @Param("c") Color c);/*Año y color*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.anioFabricacion LIKE :a AND v.precio BETWEEN :p1 AND :p2 ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerAnioPrecio(@Param("a") Integer a, @Param("p1") Double p1, @Param("p2") Double p2);/*Año y precio*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.anioFabricacion LIKE :a AND v.nuevo LIKE :n ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerAnioEstado(@Param("a") Integer a, @Param("n") Boolean n);/*Año y estado*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.anioFabricacion LIKE :a AND v.kilometraje BETWEEN :k AND :k1 ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerAnioKilometraje(@Param("a") Integer a, @Param("k") String k, @Param("k1") String k1);/*Año y Kilometros*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.anioFabricacion LIKE :a AND v.tipoDeCombustible LIKE :tdc ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerAnioTipoCombus(@Param("a") Integer a, @Param("tdc") TipoDeCombustible tdc);/*Año y Tipo combustible*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.anioFabricacion LIKE :a AND v.tipoDeVehiculo LIKE :tdv ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerAnioTipoVehi(@Param("a") Integer a, @Param("tdv") TipoDeVehiculo tdv);/*Año y Tipo combustible*/

 /*Marca*/
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.modelo LIKE :mod ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaModelo(@Param("m") String m, @Param("mod") String mod);/*Marca y modelo*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.color LIKE :c ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaColor(@Param("m") String m, @Param("c") Color c);/*Marca y Color*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.precio BETWEEN :p1 AND :p2 ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaPrecio(@Param("m") String m, @Param("p1") Double p1, @Param("p2") Double p2);/*Marca y Precio*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.nuevo LIKE :n ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaEstado(@Param("m") String m, @Param("n") Boolean n);/*Marca y Estado*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.kilometraje BETWEEN :k1 AND :k2 ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaKilometro(@Param("m") String m, @Param("k1") String k1,@Param("k2") String k2);/*Marca y Kilometros*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.tipoDeCombustible LIKE :tdc ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaTipoCombustible(@Param("m") String m, @Param("tdc") TipoDeCombustible tdc);/*Marca y TipoCombustible*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.tipoDeVehiculo LIKE :tdv ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaTipoVehiculo(@Param("m") String m, @Param("tdv") TipoDeVehiculo tdv);/*Marca y TipoVehiculo*/

 /*Modelo*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.modelo LIKE :m AND v.color LIKE :c ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerModeloColor(@Param("m") String m, @Param("c") Color c);/*Modelo y color*/

    
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.modelo LIKE :m AND v.precio BETWEEN :p1 AND :p2 ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerModeloPrecio(@Param("m") String m, @Param("p1") Double p1, @Param("p2") Double p2);/*Modelo y precio*/

    
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.modelo LIKE :m AND v.nuevo LIKE :n ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerModeloEstado(@Param("m") String m, @Param("n") Boolean n);/*Modelo y estado*/

    
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.modelo LIKE :m AND v.kilometraje BETWEEN :k1 AND :k2 ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerModeloKilometros(@Param("m") String m, @Param("k1") String k1,@Param("k2")String k2);/*Modelo y Kilometros*/

    
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.modelo LIKE :m AND v.tipoDeCombustible LIKE :tdc ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerModeloTipoCombustible(@Param("m") String m, @Param("tdc") TipoDeCombustible tdc);/*Modelo y TipoCombustible*/

    
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.modelo LIKE :m AND v.tipoDeVehiculo LIKE :tdv ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerModeloTipoVehiculo(@Param("m") String m, @Param("tdv") TipoDeVehiculo tdv);/*Modelo y Tipo de vehiculo*/

    
 /*Color*/
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.color LIKE :c AND v.precio BETWEEN :p1 AND :p2 ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerColorPrecio(@Param("c") Color c, @Param("p1") Double p1, @Param("p2") Double p2);/*Color y Precio*/

    
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.color LIKE :c AND v.nuevo LIKE :e ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerColorEstado(@Param("c") Color c, @Param("e") Boolean e);/*Color y Estado*/

    

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.color LIKE :c AND v.tipoDeCombustible LIKE :tdc ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerColorTipoCombustible(@Param("c") Color c, @Param("tdc") TipoDeCombustible tdc); /*Color y tdc*/

    
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.color LIKE :c AND v.tipoDeVehiculo LIKE :tdv ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerColorTipoVehiculo(@Param("c") Color c, @Param("tdv") TipoDeVehiculo tdv);/*Color y Tipo de vehiculo*/

    

 /*Precio*/
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.precio BETWEEN :p1 AND :p2 AND v.nuevo LIKE :e ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerPrecioEstado(@Param("p1") Double p1, @Param("p2") Double p2, @Param("e") Boolean e);/*Precio y estado*/

    
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.precio BETWEEN :p1 AND :p2 AND v.kilometraje BETWEEN :k1 AND :k2 ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerPrecioKilometraje(@Param("p1") Double p1, @Param("p2") Double p2, @Param("k1") String k1, @Param("k2") String k2);  /*Precio y Kilometros*/

   
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.precio BETWEEN :p1 AND :p2 AND v.tipoDeCombustible LIKE :tdc ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerPrecioTipoCombustible(@Param("p1") Double p1, @Param("p2") Double p2, @Param("tdc") TipoDeCombustible tdc);/*Precio y tdc*/

    
    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.precio BETWEEN :p1 AND :p2 AND v.tipoDeVehiculo LIKE :tdv ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerPrecioTipoVehiculo(@Param("p1") Double p1, @Param("p2") Double p2, @Param("tdv") TipoDeVehiculo tdv);/*Precio y tipo Veh*/

    

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.modelo LIKE :mod AND v.anioFabricacion LIKE :af ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaModeloMarcaAnio(@Param("m") String m, @Param("mod") String mod, @Param("af") Integer af);/*Marca y modelo Anio*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.color LIKE :c AND v.modelo LIKE :mod ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaModeloColor(@Param("m") String m, @Param("c") Color c, @Param("mod") String mod);/*Marca Color y modelo*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.precio BETWEEN :p1 AND :p2 AND v.modelo LIKE :mod ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaModeloPrecio(@Param("m") String m, @Param("p1") Double p1, @Param("p2") Double p2, @Param("mod") String mod);/*Marca modelo y Precio*/

    @Query("SELECT v FROM Vehiculo v WHERE v.alta = true AND v.marca LIKE :m AND v.nuevo LIKE :n AND v.modelo LIKE :mod ORDER BY v.anioFabricacion")
    public List<Vehiculo> TraerMarcaEstadoModelo(@Param("m") String m, @Param("n") Boolean n, @Param("mod") String mod);/*Marca modelo y estado*/


}
