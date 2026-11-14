package ar.edu.unju.fi.poo.service;

import java.time.LocalDate;
import java.util.List;
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;

public interface VentaService {
    
    VentaContado registrarVentaContado(VentaContado ventaContado);
    
    VentaFinanciada registrarVentaFinanciada(VentaFinanciada ventaFinanciada);
    
    List<Venta> listarTodas();
    
    List<Venta> consultarVentasPorCliente(Long clienteId);
    
    List<Venta> consultarVentasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
    
    List<Venta> filtrarPorModalidad(String modalidad);
    
}