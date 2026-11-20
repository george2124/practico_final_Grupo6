package ar.edu.unju.fi.poo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.poo.dto.CuotaResponseDTO;
import ar.edu.unju.fi.poo.dto.EstadoCuentaResponseDTO;
import ar.edu.unju.fi.poo.enums.EstadoCuota;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.Cuota;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.repository.CuotaRepository;


@Service
public class CuotaService {
	 @Autowired
	    private CuotaRepository cuotaRepository;

	    @Autowired
	    private ClienteRepository clienteRepository;

	    // 1. Consultar Estado de Cuenta
	    @Transactional(readOnly = true)
	    public EstadoCuentaResponseDTO obtenerEstadoCuenta(Long clienteId) {
	        Cliente cliente = clienteRepository.findById(clienteId)
	                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + clienteId));

	        List<Cuota> pendientes = cuotaRepository.findByClienteIdAndEstado(clienteId, EstadoCuota.ADEUDADA);
	        pendientes.addAll(cuotaRepository.findByClienteIdAndEstado(clienteId, EstadoCuota.VENCIDA));

	        long totalAdeudado = 0;
	        for (Cuota c : pendientes) {
	            totalAdeudado += c.getImporteAPagar();
	            if (c.getEstado() == EstadoCuota.ADEUDADA && LocalDate.now().isAfter(c.getFechaVencimiento())) {
	                c.setEstado(EstadoCuota.VENCIDA);
	            }
	        }

	        List<CuotaResponseDTO> cuotasDTO = pendientes.stream()
	                .map(this::convertirACuotaResponseDTO)
	                .collect(Collectors.toList());

	        String nombreCompleto = cliente.getNombre() + " " + cliente.getApellidoRazonSocial();

	        return new EstadoCuentaResponseDTO(
	                cliente.getId(),
	                nombreCompleto,
	                cuotasDTO.size(),
	                totalAdeudado,
	                cuotasDTO
	        );
	    }

	    // 2. Cobrar la siguiente cuota correlativa de una venta
	    @Transactional
	    public CuotaResponseDTO cobrarSiguienteCuota(Long ventaId) {
	        Cuota cuotaACobrar = cuotaRepository
	                .findFirstByVentaFinanciadaIdAndEstadoNotOrderByNumeroCuotaAsc(ventaId, EstadoCuota.PAGADA)
	                .orElseThrow(() -> new RecursoNoEncontradoException("No hay cuotas pendientes o la venta no existe con ID: " + ventaId));

	        if (LocalDate.now().isAfter(cuotaACobrar.getFechaVencimiento())) {
	            cuotaACobrar.setEstado(EstadoCuota.VENCIDA);
	        }

	        cuotaACobrar.setEstado(EstadoCuota.PAGADA);
	        Cuota cuotaPagada = cuotaRepository.save(cuotaACobrar);

	        boolean quedanPendientes = cuotaRepository.existsByVentaFinanciadaIdAndEstadoNot(ventaId, EstadoCuota.PAGADA);
	        if (!quedanPendientes) {
	            VentaFinanciada venta = cuotaPagada.getVentaFinanciada();
	            venta.setSaldada(true);
	        }

	        return convertirACuotaResponseDTO(cuotaPagada);
	    }

	    // Método auxiliar ajustado a los 7 parámetros exactos de tu CuotaResponseDTO
	    private CuotaResponseDTO convertirACuotaResponseDTO(Cuota c) {
	        return new CuotaResponseDTO(
	                c.getId(),
	                c.getNumeroCuota(),
	                c.getImporteAPagar(),
	                c.getFechaVencimiento(),
	                c.getEstado(),
	                c.getVentaFinanciada().getId(),
	                c.getVentaFinanciada().getCodigoComprobante()
	        );
	    }
}
