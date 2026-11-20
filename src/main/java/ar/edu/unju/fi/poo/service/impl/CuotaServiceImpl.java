package ar.edu.unju.fi.poo.service.impl;

<<<<<<< HEAD
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

    // --------------------------------------------------
    // 1. CONSULTA DEL ESTADO DE CUENTA
    // --------------------------------------------------

    @Override
    @Transactional
    public EstadoCuentaResponseDTO obtenerEstadoCuenta(Long clienteId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() ->
                    new RecursoNoEncontradoException(
                        "Cliente no encontrado con ID: " + clienteId));

        // Recuperamos todas las cuotas del cliente.
        List<Cuota> cuotas = cuotaRepository
                .buscarCuotasPorCliente(clienteId);

        LocalDate fechaActual = LocalDate.now();

        // Actualizamos automáticamente las cuotas vencidas.
        for (Cuota cuota : cuotas) {

            if (cuota.getEstado() == EstadoCuota.ADEUDADA
                    && fechaActual.isAfter(cuota.getFechaVencimiento())) {

                cuota.setEstado(EstadoCuota.VENCIDA);
            }
        }

        // Guardamos los cambios de estado.
        cuotaRepository.saveAll(cuotas);

        // Filtramos las cuotas que todavía no fueron pagadas.
        List<Cuota> cuotasPendientes = cuotas.stream()
                .filter(cuota ->
                    cuota.getEstado() != EstadoCuota.PAGADA)
                .collect(Collectors.toList());

        // Calculamos el total de la deuda.
        long totalAdeudado = cuotasPendientes.stream()
                .mapToLong(Cuota::getImporteAPagar)
                .sum();

        // Convertimos las cuotas a DTO.
        List<CuotaResponseDTO> cuotasDTO = cuotasPendientes.stream()
                .map(VentaMapper::toCuotaResponse)
                .collect(Collectors.toList());

        // Recuperamos el historial completo de ventas.
        List<Venta> historialVentas =
                ventaRepository.findByClienteId(clienteId);

        // Convertimos las ventas a DTO.
        var historialDTO = VentaMapper.toResponseList(historialVentas);

        String nombreCompleto =
                cliente.getNombre() + " "
                + cliente.getApellidoRazonSocial();

        return new EstadoCuentaResponseDTO(
                cliente.getId(),
                nombreCompleto,
                cuotasDTO.size(),
                totalAdeudado,
                cuotasDTO,
                historialDTO
        );
    }

    // --------------------------------------------------
    // 2. COBRO DE LA SIGUIENTE CUOTA
    // --------------------------------------------------

    @Override
    @Transactional
    public CuotaResponseDTO cobrarSiguienteCuota(Long ventaId) {

        // Verificamos que exista la venta.
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() ->
                    new RecursoNoEncontradoException(
                        "Venta no encontrada con ID: " + ventaId));

        // El cobro de cuotas solamente corresponde
        // a ventas financiadas.
        if (!(venta instanceof VentaFinanciada)) {
            throw new SolicitudInvalidaException(
                    "La venta indicada no es una venta financiada.");
        }

        VentaFinanciada ventaFinanciada =
                (VentaFinanciada) venta;

        // Recuperamos las cuotas ordenadas correlativamente.
        List<Cuota> cuotas =
                cuotaRepository.buscarCuotasPorVenta(ventaId);

        if (cuotas.isEmpty()) {
            throw new SolicitudInvalidaException(
                    "La venta no tiene cuotas registradas.");
        }

        // Buscamos la primera cuota que todavía no fue pagada.
        Cuota siguienteCuota = cuotas.stream()
                .filter(cuota ->
                    cuota.getEstado() != EstadoCuota.PAGADA)
                .findFirst()
                .orElseThrow(() ->
                    new SolicitudInvalidaException(
                        "La venta no tiene cuotas pendientes de pago."));

        LocalDate fechaActual = LocalDate.now();

        // No se permite cobrar una cuota antes de su vencimiento.
        if (fechaActual.isBefore(
                siguienteCuota.getFechaVencimiento())) {

            throw new SolicitudInvalidaException(
                    "No se puede cobrar la cuota "
                    + siguienteCuota.getNumeroCuota()
                    + " porque su fecha de vencimiento es futura: "
                    + siguienteCuota.getFechaVencimiento());
        }

        // Si la fecha actual supera el vencimiento,
        // la cuota pasa por el estado VENCIDA.
        if (fechaActual.isAfter(
                siguienteCuota.getFechaVencimiento())) {

            siguienteCuota.setEstado(EstadoCuota.VENCIDA);
        }

        // Registramos el pago.
        siguienteCuota.setEstado(EstadoCuota.PAGADA);

        Cuota cuotaPagada =
                cuotaRepository.save(siguienteCuota);

        // Comprobamos que no queden compromisos pendientes.
        boolean quedanCuotasPendientes = cuotas.stream()
                .anyMatch(cuota ->
                    cuota.getEstado() != EstadoCuota.PAGADA);

        // La venta solamente queda saldada cuando
        // todas sus cuotas están pagadas.
        ventaFinanciada.setSaldada(!quedanCuotasPendientes);

        ventaRepository.save(ventaFinanciada);

        // Devolvemos la cuota pagada mediante DTO.
        return VentaMapper.toCuotaResponse(cuotaPagada);
    }
}

=======
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
>>>>>>> refs/remotes/origin/fix/desacoplamiento-dto
