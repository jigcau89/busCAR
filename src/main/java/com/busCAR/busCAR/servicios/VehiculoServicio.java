package com.busCAR.busCAR.servicios;

import com.busCAR.busCAR.entidades.Foto;
import com.busCAR.busCAR.entidades.Usuario;
import com.busCAR.busCAR.entidades.Vehiculo;
import com.busCAR.busCAR.enumeraciones.Color;
import com.busCAR.busCAR.enumeraciones.TipoDeCombustible;
import com.busCAR.busCAR.enumeraciones.TipoDeVehiculo;
import com.busCAR.busCAR.errores.ErrorServicio;
import com.busCAR.busCAR.repositorios.UsuarioRepositorio;
import com.busCAR.busCAR.repositorios.VehiculoRepositorio;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VehiculoServicio {

    @Autowired
    private VehiculoRepositorio vehiculorepositorio;
    @Autowired
    private UsuarioRepositorio usuariorepositorio;
    
    @Autowired UsuarioServicio usuarioservicio;

    @Autowired
    private FotoServicio fotoServicio;

    public void validar(String patente, String modelo, String marca, Integer anioFabricacion, Color color, Double precio, Boolean nuevo, String kilometraje, TipoDeCombustible tdc, TipoDeVehiculo tdv) throws ErrorServicio {

        if (patente == null || patente.trim().isEmpty()) {
            throw new ErrorServicio("La patente no es válida. SERVICE");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new ErrorServicio("El modelo no es válido. SERVICE");
        }
        if (marca == null || marca.trim().isEmpty()) {
            throw new ErrorServicio("La marca no es válida. SERVICE");
        }
        if (anioFabricacion == null) {
            throw new ErrorServicio("El Año de fabricación está vacío. SERVICE");
        }
        if (anioFabricacion < 1920 || anioFabricacion > 2022) {
            throw new ErrorServicio("El Año de fabricación no es válido. SERVICE");
        }

        if (color == null) {
            throw new ErrorServicio("Color está vacío. SERVICE");
        }
        if (precio == null || precio < 0) {
            throw new ErrorServicio("El precio no es válido. SERVICE");
        }
        if (nuevo == null) {
            throw new ErrorServicio("Debe indicar estado de uso del vehículo. SERVICE");
        }
        if (kilometraje == null || kilometraje.trim().isEmpty()) {
            throw new ErrorServicio("El kilometraje no es válido. SERVICE");
        }
        if (kilometraje.length() < 2 || kilometraje.length() > 7) {
            throw new ErrorServicio("El rango del Kilometraje es incorrecto. SERVICE");
        }
        for (int x = 0; x < kilometraje.length(); x++) {
            char c = kilometraje.charAt(x);
            if (!(c >= '0' && c <= '9')) {
                throw new ErrorServicio("El kilometraje no puede tener caracteres no numéricos. SERVICE");
                /* valida si hay caracteres no numericos en kilometraje "PROBAR"*/
            }
        }
        if (tdc == null) {
            throw new ErrorServicio("El tipo de combustible está vacío. SERVICE");
        }
        if (tdv == null) {
            throw new ErrorServicio("El tipo de vehículo está vacío. SERVICE");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void guardar(MultipartFile archivo, String patente, String modelo, String marca, Integer anioFabricacion, Color color, Double precio, Boolean nuevo, String kilometraje, TipoDeCombustible tdc, String descripcion, boolean alta, TipoDeVehiculo tdv, Usuario usuario) throws ErrorServicio {

        try {
            validar(patente, modelo, marca, anioFabricacion, color, precio, nuevo, kilometraje, tdc, tdv);
            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setPatente(patente);
            vehiculo.setModelo(modelo);
            vehiculo.setMarca(marca);
            vehiculo.setAnioFabricacion(anioFabricacion);
            vehiculo.setColor(color);
            vehiculo.setNuevo(nuevo);
            vehiculo.setPrecio(precio);
            vehiculo.setKilometraje(kilometraje);
            vehiculo.setTipoDeCombustible(tdc);
            vehiculo.setDescripcion(descripcion);
            vehiculo.setAlta(Boolean.TRUE);
            vehiculo.setTipoDeVehiculo(tdv);
            Foto foto = fotoServicio.guardar(archivo);
            vehiculo.setFotos(foto);
            vehiculo.setUsuario(usuario);
            vehiculorepositorio.save(vehiculo);

        } catch (ErrorServicio e) {
            throw new ErrorServicio(e.getMessage());
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void modificar(String id_usuario, String id, MultipartFile archivo, String patente, String modelo, String marca, Integer anioFabricacion, Color color, Double precio, Boolean nuevo, String kilometraje, TipoDeCombustible tdc, String descripcion, boolean alta, TipoDeVehiculo tdv) throws ErrorServicio {
        try {
            validar(patente, modelo, marca, anioFabricacion, color, precio, nuevo, kilometraje, tdc, tdv);
            Usuario usuario = usuarioservicio.buscarPorIdUsuario(id_usuario);
            //String id2 = vehiculorepositorio.buscarVehiculoPorIdUsuario(id_usuario);
            String id2 = "3f04e8a0-7a67-4081-a75b-bf9b0c5634a9";
            Optional<Vehiculo> respuesta = vehiculorepositorio.findById(id2);
            
            if (respuesta.isPresent()) {

                Vehiculo vehiculo = respuesta.get();

                if (id2.equals(id_usuario)) {
                    vehiculo.setPatente(patente);
                    vehiculo.setModelo(modelo);
                    vehiculo.setMarca(marca);
                    vehiculo.setAnioFabricacion(anioFabricacion);
                    vehiculo.setColor(color);
                    vehiculo.setNuevo(nuevo);
                    vehiculo.setPrecio(precio);
                    vehiculo.setKilometraje(kilometraje);
                    vehiculo.setTipoDeCombustible(tdc);
                    vehiculo.setDescripcion(descripcion);
                    vehiculo.setAlta(alta);
                    vehiculo.setTipoDeVehiculo(tdv);
                    vehiculo.setUsuario(usuario);
                    String idFoto = null;
                    if (vehiculo.getFotos() != null) {
                        idFoto = vehiculo.getFotos().getId();
                    }

                    Foto foto = fotoServicio.actualizar(idFoto, archivo);
                    vehiculo.setFotos(foto);

                    vehiculorepositorio.save(vehiculo);
                } 
                else {
                    throw new ErrorServicio("MODIFICAR: IDS no coinciden");
                }

            } else {
                throw new ErrorServicio("MODIFICAR: No se encontró vehículo solicitado");

            }

        } catch (ErrorServicio e) {
            throw new ErrorServicio(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void borrar(String id) {
        try {
            Optional<Vehiculo> respuesta = vehiculorepositorio.findById(id);
            if (respuesta.isPresent()) {
                Vehiculo vehiculo = respuesta.get();
                vehiculorepositorio.delete(vehiculo);
            } else {
                throw new ErrorServicio("ELIMINAR: No se encontró vehículo solicitado");
            }
        } catch (ErrorServicio e) {

        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Vehiculo> respuesta = vehiculorepositorio.findById(id);

        if (respuesta.isPresent()) {
            Vehiculo vehiculo = respuesta.get();
            vehiculo.setNuevo(Boolean.FALSE);
            vehiculorepositorio.save(vehiculo);
        } else {
            throw new ErrorServicio("ALTA: El vehículo no se encontró.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void pasarUsuarioVehiculo(HttpSession session) {

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void habilitar(String id) throws ErrorServicio {
        Optional<Vehiculo> respuesta = vehiculorepositorio.findById(id);

        if (respuesta.isPresent()) {
            Vehiculo vehiculo = respuesta.get();
            vehiculo.setNuevo(Boolean.TRUE);
            vehiculorepositorio.save(vehiculo);
        } else {
            throw new ErrorServicio("ALTA: El vehículo no se encontró.");
        }
    }

    /*Búsquedas*/
    public Vehiculo buscarPorId(String id)throws ErrorServicio {

        Optional<Vehiculo> respuesta = vehiculorepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("El usuario no existe" + id);
                    
        }

    }
    @Transactional(readOnly = true)
    public String buscarVehiculoPorIdUsuario(String id)throws ErrorServicio {

           
            String id2 = "d46b1fe3-9615-478d-b4fd-92483705fecd";
            return vehiculorepositorio.buscarVehiculoPorIdUsuario(id2);   

    }
    
    
    
    
    
     @Transactional(readOnly = true)
    public List<Vehiculo> buscarTodos() {
        return vehiculorepositorio.findAll();
        /*Traer todos*/
    }
    

    /*Listas de todos los vehículos */
    @Transactional(readOnly = true)
    public List<Vehiculo> buscarRelacionados(TipoDeVehiculo tv, String id_vehiculo) {
        return vehiculorepositorio.buscarRelacionados(tv, id_vehiculo);
        /*Traer todos*/
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> TraerNuevos() {
        return vehiculorepositorio.TraerNuevos();
        /*Traer nuevos */
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> TraerUsados() {
        return vehiculorepositorio.TraerUsados();
        /*Traer USADOS */
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> ListaVehiculosOrdenadoAnio() {
        return vehiculorepositorio.ListaVehiculosOrdenadoAnio();
        /* Por Año*/
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> ListaVehiculosOrdenadoMarca() {
        return vehiculorepositorio.ListaVehiculosOrdenadoMarca();
        /* Por Marca*/
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> ListaVehiculosOrdenadoModelo() {
        return vehiculorepositorio.ListaVehiculosOrdenadoModelo();
        /* Por Modelo*/
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> ListaVehiculosOrdenadoTipo() {
        return vehiculorepositorio.ListaVehiculosOrdenadoTipo();
        /* Por Tipo*/
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> ListaVehiculosOrdenadoKm() {
        return vehiculorepositorio.ListaVehiculosOrdenadoKm();
        /* Por Km*/
    }

    /*CON PARÁMETROS*/
 /*por año*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerPorAnio(Integer af) {
        return vehiculorepositorio.TraerPorAnio(af);
    }

    /*Por Modelo*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerPorModelo(String m) {
        return vehiculorepositorio.TraerPorModelo(m);
    }

    /*Por Marca*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerPorMarca(String m) {
        return vehiculorepositorio.TraerPorMarca(m);
    }

    /*Por Color*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerPorColor(Color c) {
        return vehiculorepositorio.TraerPorColor(c);
    }

    /*Por tipo de combustible*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerPorTcombustible(TipoDeCombustible tc) {
        return vehiculorepositorio.TraerPorTcombustible(tc);
    }

    /*Por tipo de Vehículo*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerPorTipov(TipoDeVehiculo tv) {
        return vehiculorepositorio.TraerPorTipov(tv);
    }

    /*LISTAS COMPUESTAS CON + DE 1 PARÁMETRO*/
 /*Año y Marca*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerAnioMarca(Integer a, String m) {
        return vehiculorepositorio.TraerAnioMarca(a, m);
    }

    /*Año y modelo*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerAnioModelo(Integer a, String m) {
        return vehiculorepositorio.TraerAnioModelo(a, m);
    }

    /*Año y color*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerAnioColor(Integer a, Color c) {
        return vehiculorepositorio.TraerAnioColor(a, c);
    }

    /*Año y precio*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerAnioPrecio(Integer a, Double p1, Double p2) {
        return vehiculorepositorio.TraerAnioPrecio(a, p1, p2);
    }

    /*Año y estado*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerAnioPrecio(Integer a, Boolean e) {
        return vehiculorepositorio.TraerAnioEstado(a, e);
    }

    /*Año y kilometraje*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerAnioKilometraje(Integer a, String k1, String k2) {
        return vehiculorepositorio.TraerAnioKilometraje(a, k1, k2);
    }

    /*Año y Tipo de combustible*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerAnioTipoCombus(Integer a, TipoDeCombustible tdc) {
        return vehiculorepositorio.TraerAnioTipoCombus(a, tdc);
    }

    /*Año y Tipo de vehículo*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerAnioTipoVehi(Integer a, TipoDeVehiculo tdv) {
        return vehiculorepositorio.TraerAnioTipoVehi(a, tdv);
    }

    /*Marca y Modelo*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerMarcaModelo(String mar, String mod) {
        return vehiculorepositorio.TraerMarcaModelo(mar, mod);
    }

    /*Marca y Color*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerMarcaColor(String mar, Color c) {
        return vehiculorepositorio.TraerMarcaColor(mar, c);
    }

    /*Marca y precio*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerMarcaPrecio(String mar, Double p1, Double p2) {
        return vehiculorepositorio.TraerMarcaPrecio(mar, p1, p2);
    }

    /*Marca y estado*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerMarcaEstado(String mar, Boolean est) {
        return vehiculorepositorio.TraerMarcaEstado(mar, est);
    }

    /*Marca y Kilometraje*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerMarcaKilometro(String mar, String k1, String k2) {
        return vehiculorepositorio.TraerMarcaKilometro(mar, k1, k2);
    }

    /*Marca y Tipo de Combustible*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerMarcaTipoCombustible(String m, TipoDeCombustible tdc) {
        return vehiculorepositorio.TraerMarcaTipoCombustible(m, tdc);
    }

    /*Marca y Tipo de vehículo*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerMarcaTipoVehiculo(String m, TipoDeVehiculo tdv) {
        return vehiculorepositorio.TraerMarcaTipoVehiculo(m, tdv);
    }

    /*Modelo y Color*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerModeloColor(String m, Color c) {
        return vehiculorepositorio.TraerModeloColor(m, c);
    }

    /*Modelo y precio*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerModeloPrecio(String m, Double p1, Double p2) {
        return vehiculorepositorio.TraerModeloPrecio(m, p1, p2);
    }

    /*Modelo y estado*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerModeloEstado(String m, Boolean e) {
        return vehiculorepositorio.TraerModeloEstado(m, e);
    }

    /*Modelo y Kilometros*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerModeloKilometros(String m, String k1, String k2) {
        return vehiculorepositorio.TraerModeloKilometros(m, k1, k2);
    }

    /*Modelo y TipoCombustible*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerModeloTipoCombustible(String m, TipoDeCombustible tdc) {
        return vehiculorepositorio.TraerModeloTipoCombustible(m, tdc);
    }

    /*Modelo y Tipo de vehiculo*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerModeloTipoVehiculo(String m, TipoDeVehiculo tdv) {
        return vehiculorepositorio.TraerModeloTipoVehiculo(m, tdv);
    }

    /*Color y Precio*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerColorPrecio(Color c, Double p1, Double p2) {
        return vehiculorepositorio.TraerColorPrecio(c, p1, p2);
    }

    /*Color y Estado*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerColorEstado(Color c, Boolean e) {
        return vehiculorepositorio.TraerColorEstado(c, e);
    }

    /*Color y Tipo de combustible*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerColorTipoCombustible(Color c, TipoDeCombustible tdc) {
        return vehiculorepositorio.TraerColorTipoCombustible(c, tdc);
    }

    /*Color y Tipo de vehiculo*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerColorTipoVehiculo(Color c, TipoDeVehiculo tdv) {
        return vehiculorepositorio.TraerColorTipoVehiculo(c, tdv);
    }

    /*Precio y Estado*/
    @Transactional(readOnly = true)
    public List<Vehiculo> TraerPrecioEstado(Double p1, Double p2, Boolean e) {
        return vehiculorepositorio.TraerPrecioEstado(p1, p2, e);
    }

    /*Precio y Kilometraje*/
    public List<Vehiculo> TraerPrecioKilometraje(Double p1, Double p2, String k1, String k2) {
        return vehiculorepositorio.TraerPrecioKilometraje(p1, p2, k1, k2);
    }

    /*Precio y tipo de combustible*/
    public List<Vehiculo> TraerPrecioTipoCombustible(Double p1, Double p2, TipoDeCombustible tdc) {
        return vehiculorepositorio.TraerPrecioTipoCombustible(p1, p2, tdc);
    }

    /*Precio y tipo de Vehículo*/
    public List<Vehiculo> TraerPrecioTipoVehiculo(Double p1, Double p2, TipoDeVehiculo tdv) {
        return vehiculorepositorio.TraerPrecioTipoVehiculo(p1, p2, tdv);
    }

    /*Marca Modelo y año*/
    public List<Vehiculo> TraerMarcaModeloMarcaAnio(String mar, String mod, Integer anio) {
        return vehiculorepositorio.TraerMarcaModeloMarcaAnio(mar, mod, anio);
    }

    /*Marca Color y modelo*/
    public List<Vehiculo> TraerMarcaModeloColor(String mar, Color c, String mod) {
        return vehiculorepositorio.TraerMarcaModeloColor(mar, c, mod);
    }

    /*Marca modelo y Precio*/
    public List<Vehiculo> TraerMarcaModeloPrecio(String mar, Double p1, Double p2, String mod) {
        return vehiculorepositorio.TraerMarcaModeloPrecio(mar, p1, p2, mod);
    }

    /*Marca modelo y estado*/
    public List<Vehiculo> TraerMarcaEstadoModelo(String mar, Boolean e, String mod) {
        return vehiculorepositorio.TraerMarcaEstadoModelo(mar, e, mod);
    }

}
