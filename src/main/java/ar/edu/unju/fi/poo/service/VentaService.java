package ar.edu.unju.fi.poo.service;

import java.time.LocalDate;
import java.util.List;
<<<<<<< HEAD
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
=======

import ar.edu.unju.fi.poo.dto.VentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;

public interface VentaService {
>>>>>>> refs/remotes/origin/fix/desacoplamiento-dto

<<<<<<< HEAD
public interface VentaService {
    
    VentaContado registrarVentaContado(VentaContado ventaContado);
    
    VentaFinanciada registrarVentaFinanciada(VentaFinanciada ventaFinanciada);
    
    List<Venta> listarTodas();
    
    List<Venta> consultarVentasPorCliente(Long clienteId);
    
    List<Venta> consultarVentasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
    
    List<Venta> filtrarPorModalidad(String modalidad);
    
=======
    VentaResponseDTO registrarVentaContado(VentaRequestDTO request);

    VentaResponseDTO registrarVentaFinanciada(VentaRequestDTO request);

    List<VentaResponseDTO> listarTodas();

    List<VentaResponseDTO> consultarVentasPorCliente(Long clienteId);

    List<VentaResponseDTO> consultarVentasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    List<VentaResponseDTO> filtrarPorModalidad(String modalidad);
>>>>>>> refs/remotes/origin/fix/desacoplamiento-dto
}