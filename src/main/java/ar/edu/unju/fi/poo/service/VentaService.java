package ar.edu.unju.fi.poo.service;

import java.time.LocalDate;
import java.util.List;

import ar.edu.unju.fi.poo.dto.VentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;

public interface VentaService {

    VentaResponseDTO registrarVentaContado(VentaRequestDTO request);

    VentaResponseDTO registrarVentaFinanciada(VentaRequestDTO request);

    List<VentaResponseDTO> listarTodas();

    List<VentaResponseDTO> consultarVentasPorCliente(Long clienteId);

    List<VentaResponseDTO> consultarVentasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    List<VentaResponseDTO> filtrarPorModalidad(String modalidad);
}