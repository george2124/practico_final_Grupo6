package ar.edu.unju.fi.poo.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.poo.dto.CuotaResponseDTO;
import ar.edu.unju.fi.poo.dto.EstadoCuentaResponseDTO;
import ar.edu.unju.fi.poo.enums.EstadoCuota;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.exception.SolicitudInvalidaException;
import ar.edu.unju.fi.poo.mapper.VentaMapper;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.Cuota;
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.repository.CuotaRepository;
import ar.edu.unju.fi.poo.repository.VentaRepository;
import ar.edu.unju.fi.poo.service.CuotaService;

@Service
public class CuotaServiceImpl implements CuotaService {

    private final CuotaRepository cuotaRepository;
    private final ClienteRepository clienteRepository;
    private final VentaRepository ventaRepository;

    public CuotaServiceImpl(
            CuotaRepository cuotaRepository,
            ClienteRepository clienteRepository,
            VentaRepository ventaRepository) {

        this.cuotaRepository = cuotaRepository;
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional
    public EstadoCuentaResponseDTO obtenerEstadoCuenta(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + clienteId));

        List<Cuota> cuotas = cuotaRepository.buscarCuotasPorCliente(clienteId);
        LocalDate fechaActual = LocalDate.now();

        for (Cuota cuota : cuotas) {
            if (cuota.getEstado() == EstadoCuota.ADEUDADA
                    && fechaActual.isAfter(cuota.getFechaVencimiento())) {
                cuota.setEstado(EstadoCuota.VENCIDA);
            }
        }

        cuotaRepository.saveAll(cuotas);

        List<Cuota> cuotasPendientes = cuotas.stream()
                .filter(cuota -> cuota.getEstado() != EstadoCuota.PAGADA)
                .collect(Collectors.toList());

        long totalAdeudado = cuotasPendientes.stream()
                .mapToLong(Cuota::getImporteAPagar)
                .sum();

        List<CuotaResponseDTO> cuotasDTO = cuotasPendientes.stream()
                .map(VentaMapper::toCuotaResponse)
                .collect(Collectors.toList());

        List<Venta> historialVentas = ventaRepository.findByClienteId(clienteId);
        var historialDTO = VentaMapper.toResponseList(historialVentas);

        String nombreCompleto = cliente.getNombre() + " " + cliente.getApellidoRazonSocial();

        return new EstadoCuentaResponseDTO(
                cliente.getId(),
                nombreCompleto,
                cuotasDTO.size(),
                totalAdeudado,
                cuotasDTO,
                historialDTO
        );
    }

    @Override
    @Transactional
    public CuotaResponseDTO cobrarSiguienteCuota(Long ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + ventaId));

        if (!(venta instanceof VentaFinanciada)) {
            throw new SolicitudInvalidaException("La venta indicada no es una venta financiada.");
        }

        VentaFinanciada ventaFinanciada = (VentaFinanciada) venta;

        List<Cuota> cuotas = cuotaRepository.buscarCuotasPorVenta(ventaId);
        if (cuotas.isEmpty()) {
            throw new SolicitudInvalidaException("La venta no tiene cuotas registradas.");
        }

        Cuota siguienteCuota = cuotas.stream()
                .filter(cuota -> cuota.getEstado() != EstadoCuota.PAGADA)
                .findFirst()
                .orElseThrow(() -> new SolicitudInvalidaException("La venta no tiene cuotas pendientes de pago."));

        LocalDate fechaActual = LocalDate.now();

        if (fechaActual.isAfter(siguienteCuota.getFechaVencimiento())) {
            siguienteCuota.setEstado(EstadoCuota.VENCIDA);
        }

        siguienteCuota.setEstado(EstadoCuota.PAGADA);
        Cuota cuotaPagada = cuotaRepository.save(siguienteCuota);

        boolean quedanCuotasPendientes = cuotas.stream()
                .anyMatch(cuota -> cuota.getEstado() != EstadoCuota.PAGADA);

        ventaFinanciada.setSaldada(!quedanCuotasPendientes);
        ventaRepository.save(ventaFinanciada);

        return VentaMapper.toCuotaResponse(cuotaPagada);
    }
}