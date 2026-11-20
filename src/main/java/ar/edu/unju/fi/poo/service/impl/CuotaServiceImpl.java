package ar.edu.unju.fi.poo.service.impl;

import java.util.Comparator;
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
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.repository.CuotaRepository;
import ar.edu.unju.fi.poo.repository.VentaRepository;
import ar.edu.unju.fi.poo.service.CuotaService;

@Service
public class CuotaServiceImpl implements CuotaService {

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    @Transactional(readOnly = true)
    public EstadoCuentaResponseDTO obtenerEstadoCuenta(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + clienteId));

        List<Cuota> pendientes = cuotaRepository.findByVentaClienteIdAndEstado(clienteId, EstadoCuota.ADEUDADA)
                .stream()
                .sorted(Comparator.comparing(Cuota::getFechaVencimiento).thenComparing(Cuota::getNumeroCuota))
                .collect(Collectors.toList());

        List<CuotaResponseDTO> cuotasDTO = pendientes.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());

        long totalAdeudado = pendientes.stream()
                .mapToLong(Cuota::getImporteAPagar)
                .sum();

        EstadoCuentaResponseDTO estadoCuenta = new EstadoCuentaResponseDTO();
        estadoCuenta.setClienteId(cliente.getId());
        estadoCuenta.setClienteNombre(cliente.getNombre() + " " + cliente.getApellidoRazonSocial());
        estadoCuenta.setCantidadCuotasPendientes(cuotasDTO.size());
        estadoCuenta.setTotalAdeudado(totalAdeudado);
        estadoCuenta.setCuotasPendientes(cuotasDTO);

        return estadoCuenta;
    }

    @Override
    @Transactional
    public CuotaResponseDTO cobrarSiguienteCuota(Long ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + ventaId));

        Cuota siguienteCuota = cuotaRepository.findByVentaIdAndEstadoOrderByNumeroCuotaAsc(venta.getId(), EstadoCuota.ADEUDADA)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No hay cuotas pendientes de pago para la venta con ID: " + ventaId));

        siguienteCuota.setEstado(EstadoCuota.PAGADA);
        Cuota cuotaActualizada = cuotaRepository.save(siguienteCuota);

        return convertirAResponseDTO(cuotaActualizada);
    }

    private CuotaResponseDTO convertirAResponseDTO(Cuota cuota) {
        CuotaResponseDTO dto = new CuotaResponseDTO();
        dto.setId(cuota.getId());
        dto.setNumeroCuota(cuota.getNumeroCuota());
        dto.setImporteAPagar(cuota.getImporteAPagar());
        dto.setFechaVencimiento(cuota.getFechaVencimiento());
        dto.setEstado(cuota.getEstado());

        if (cuota.getVenta() != null) {
            dto.setVentaId(cuota.getVenta().getId());
            dto.setCodigoComprobante(cuota.getVenta().getCodigoComprobante());
        }

        return dto;
    }
}