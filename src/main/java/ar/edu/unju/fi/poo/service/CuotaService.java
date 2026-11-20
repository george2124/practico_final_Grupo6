package ar.edu.unju.fi.poo.service;

import ar.edu.unju.fi.poo.dto.CuotaResponseDTO;
import ar.edu.unju.fi.poo.dto.EstadoCuentaResponseDTO;

public interface CuotaService {

    EstadoCuentaResponseDTO obtenerEstadoCuenta(Long clienteId);

    CuotaResponseDTO cobrarSiguienteCuota(Long ventaId);
}
