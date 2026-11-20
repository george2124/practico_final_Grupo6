package ar.edu.unju.fi.poo.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.poo.dto.VentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;
import ar.edu.unju.fi.poo.enums.EstadoCuota;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.exception.StockInsuficienteException;
import ar.edu.unju.fi.poo.mapper.VentaMapper;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.Cuota;
import ar.edu.unju.fi.poo.model.DetalleVenta;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.repository.ProductoRepository;
import ar.edu.unju.fi.poo.repository.VentaRepository;
import ar.edu.unju.fi.poo.service.VentaService;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional
    public VentaResponseDTO registrarVentaContado(VentaRequestDTO request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + request.getClienteId()));

        VentaContado venta = new VentaContado();
        venta.setCodigoComprobante(resolverCodigoComprobante(request.getCodigoComprobante()));
        venta.setFecha(LocalDate.now());
        venta.setCliente(cliente);

        boolean cuponValido = Boolean.TRUE.equals(request.getCuponValido());
        long porcentajeDescuento = request.getPorcentajeDescuento() != null ? request.getPorcentajeDescuento() : 0L;
        venta.setCuponDescuentoValido(cuponValido);

        long subtotalGeneral = procesarDetallesYStock(venta, request);
        venta.setSubtotal(subtotalGeneral);

        long montoDesc = 0L;
        if (cuponValido && porcentajeDescuento > 0L) {
            montoDesc = (subtotalGeneral * porcentajeDescuento) / 100L;
        }
        venta.setMontoDescuento(montoDesc);
        venta.setTotal(subtotalGeneral - montoDesc);

        VentaContado guardada = ventaRepository.save(venta);
        return VentaMapper.toResponse(guardada);
    }

    @Override
    @Transactional
    public VentaResponseDTO registrarVentaFinanciada(VentaRequestDTO request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + request.getClienteId()));

        VentaFinanciada venta = new VentaFinanciada();
        venta.setCodigoComprobante(resolverCodigoComprobante(request.getCodigoComprobante()));
        venta.setFecha(LocalDate.now());
        venta.setCliente(cliente);

        long porcentajeRecargo = request.getPorcentajeRecargo() != null ? request.getPorcentajeRecargo() : 0L;
        venta.setPorcentajeRecargo(porcentajeRecargo);

        long subtotalGeneral = procesarDetallesYStock(venta, request);
        venta.setSubtotal(subtotalGeneral);

        long montoRecargo = (subtotalGeneral * porcentajeRecargo) / 100L;
        long totalFinanciado = subtotalGeneral + montoRecargo;
        venta.setTotal(totalFinanciado);

        VentaFinanciada ventaGuardada = ventaRepository.save(venta);

        long importeCuota = totalFinanciado / 6L;
        long resto = totalFinanciado % 6L;

        for (int i = 1; i <= 6; i++) {
            long importeFinalCuota = importeCuota + (i == 1 ? resto : 0L);
            LocalDate vencimiento = LocalDate.now().plusMonths(i);

            Cuota cuota = new Cuota(null, i, importeFinalCuota, vencimiento, EstadoCuota.ADEUDADA, ventaGuardada);
            ventaGuardada.agregarCuota(cuota);
        }

        return VentaMapper.toResponse(ventaRepository.save(ventaGuardada));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> listarTodas() {
        return VentaMapper.toResponseList(ventaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> consultarVentasPorCliente(Long clienteId) {
        return VentaMapper.toResponseList(ventaRepository.findByClienteId(clienteId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> consultarVentasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return VentaMapper.toResponseList(ventaRepository.findByFechaBetween(fechaInicio, fechaFin));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> filtrarPorModalidad(String modalidad) {
        List<Venta> ventas = ventaRepository.findAll().stream()
                .filter(v -> {
                    if ("CONTADO".equalsIgnoreCase(modalidad)) {
                        return v instanceof VentaContado;
                    }
                    if ("FINANCIADA".equalsIgnoreCase(modalidad)) {
                        return v instanceof VentaFinanciada;
                    }
                    return true;
                })
                .toList();
        return VentaMapper.toResponseList(ventas);
    }

    private String resolverCodigoComprobante(String codigo) {
        if (codigo != null && !codigo.isBlank()) {
            return codigo.trim();
        }
        return "COMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private long procesarDetallesYStock(Venta venta, VentaRequestDTO request) {
        long subtotalGeneral = 0L;
        if (request.getItems() != null) {
            for (var item : request.getItems()) {
                Producto producto = productoRepository.findById(item.getProductoId())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + item.getProductoId()));

                if (producto.getStock() < item.getCantidad()) {
                    throw new StockInsuficienteException("Stock insuficiente para el producto: " + producto.getNombre()
                            + ". Stock actual: " + producto.getStock() + ", solicitado: " + item.getCantidad());
                }

                producto.setStock(producto.getStock() - item.getCantidad());
                productoRepository.save(producto);

                long subtotalDetalle = producto.getPrecioBase() * item.getCantidad();
                subtotalGeneral += subtotalDetalle;

                DetalleVenta detalle = new DetalleVenta(null, item.getCantidad(), producto.getPrecioBase(), subtotalDetalle, producto, venta);
                venta.agregarDetalle(detalle);
            }
        }
        return subtotalGeneral;
    }
}