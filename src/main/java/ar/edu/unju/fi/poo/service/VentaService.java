package ar.edu.unju.fi.poo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.poo.enums.EstadoCuota;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.exception.StockInsuficienteException;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.Cuota;
import ar.edu.unju.fi.poo.model.DetalleVenta;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.repository.ProductoRepository;
import ar.edu.unju.fi.poo.repository.VentaRepository;

import java.util.UUID;
import java.util.stream.Collectors;

import ar.edu.unju.fi.poo.dto.CuotaResponseDTO;
import ar.edu.unju.fi.poo.dto.EstadoCuentaResponseDTO;
import ar.edu.unju.fi.poo.dto.ItemVentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;
import ar.edu.unju.fi.poo.enums.ModalidadPago;
import ar.edu.unju.fi.poo.exception.SolicitudInvalidaException;
import ar.edu.unju.fi.poo.mapper.VentaMapper;
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.repository.CuotaRepository;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private CuotaRepository cuotaRepository;

    // Clase auxiliar interna para recibir los ítems de compra fácilmente
    public static class ItemCompraDTO {
        private Long productoId;
        private Integer cantidad;

        public ItemCompraDTO(Long productoId, Integer cantidad) {
            this.productoId = productoId;
            this.cantidad = cantidad;
        }

        public Long getProductoId() { return productoId; }
        public Integer getCantidad() { return cantidad; }
    }

    /**
     * Registra una Venta de Contado
     */
    @Transactional
    public VentaContado registrarVentaContado(String codigoComprobante, Long clienteId, 
                                              List<ItemCompraDTO> items, boolean cuponValido, long porcentajeDescuento) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + clienteId));

        VentaContado venta = new VentaContado();
        venta.setCodigoComprobante(codigoComprobante);
        venta.setFecha(LocalDate.now());
        venta.setCliente(cliente);
        venta.setCuponDescuentoValido(cuponValido);

        long subtotalGeneral = 0;

        for (ItemCompraDTO item : items) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + item.getProductoId()));

            // Verificar stock
            if (producto.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para el producto: " + producto.getNombre() 
                    + ". Stock actual: " + producto.getStock() + ", solicitado: " + item.getCantidad());
            }

            // Actualizar stock del producto
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            // Crear detalle de venta
            long subtotalDetalle = producto.getPrecioBase() * item.getCantidad();
            subtotalGeneral += subtotalDetalle;

            DetalleVenta detalle = new DetalleVenta(null, item.getCantidad(), producto.getPrecioBase(), subtotalDetalle, producto, venta);
            venta.agregarDetalle(detalle);
        }

        venta.setSubtotal(subtotalGeneral);

        // Aplicar descuento si el cupón es válido (ej: porcentaje sobre el subtotal)
        long montoDesc = 0;
        if (cuponValido && porcentajeDescuento > 0) {
            montoDesc = (subtotalGeneral * porcentajeDescuento) / 100;
        }
        venta.setMontoDescuento(montoDesc);
        venta.setTotal(subtotalGeneral - montoDesc);

        return ventaRepository.save(venta);
    }

    /**
     * Registra una Venta Financiada (Plan 6 Cuotas)
     */
    @Transactional
    public VentaFinanciada registrarVentaFinanciada(String codigoComprobante, Long clienteId, 
                                                    List<ItemCompraDTO> items, long porcentajeRecargo) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + clienteId));

        VentaFinanciada venta = new VentaFinanciada();
        venta.setCodigoComprobante(codigoComprobante);
        venta.setFecha(LocalDate.now());
        venta.setCliente(cliente);
        venta.setPorcentajeRecargo(porcentajeRecargo);

        long subtotalGeneral = 0;

        for (ItemCompraDTO item : items) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + item.getProductoId()));

            // Verificar stock
            if (producto.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para el producto: " + producto.getNombre() 
                    + ". Stock actual: " + producto.getStock() + ", solicitado: " + item.getCantidad());
            }

            // Actualizar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            long subtotalDetalle = producto.getPrecioBase() * item.getCantidad();
            subtotalGeneral += subtotalDetalle;

            DetalleVenta detalle = new DetalleVenta(null, item.getCantidad(), producto.getPrecioBase(), subtotalDetalle, producto, venta);
            venta.agregarDetalle(detalle);
        }

        venta.setSubtotal(subtotalGeneral);

        // Calcular recargo financiero sobre el total
        long montoRecargo = (subtotalGeneral * porcentajeRecargo) / 100;
        long totalFinanciado = subtotalGeneral + montoRecargo;
        venta.setTotal(totalFinanciado);

        // Guardar venta financiada primero para obtener su ID
        VentaFinanciada ventaGuardada = ventaRepository.save(venta);

        // Generar el plan estricto de 6 cuotas consecutivas
        long importeCuota = totalFinanciado / 6;
        long resto = totalFinanciado % 6; // Por si hay centavos/pesos que no dividen exacto

        for (int i = 1; i <= 6; i++) {
            long importeFinalCuota = importeCuota + (i == 1 ? resto : 0); // El resto se suma a la primera cuota
            LocalDate vencimiento = LocalDate.now().plusMonths(i);

            Cuota cuota = new Cuota(null, i, importeFinalCuota, vencimiento, EstadoCuota.ADEUDADA, ventaGuardada);
            ventaGuardada.agregarCuota(cuota);
        }

        return ventaRepository.save(ventaGuardada);
    }
    
    // =====================================================================
    //  TP11 - Capa de presentación: métodos que trabajan con DTOs.
    //  El mapeo entidad -> DTO se hace acá, dentro de la transacción,
    //  porque las relaciones (cliente, detalles, productos) son LAZY.
    // =====================================================================

    /**
     * Punto de entrada del POST /api/ventas. Valida la solicitud y delega en
     * registrarVentaContado / registrarVentaFinanciada (que verifican stock,
     * aplican descuento o recargo y generan el plan de 6 cuotas).
     */
    @Transactional
    public VentaResponseDTO registrarVenta(VentaRequestDTO request) {
        validarSolicitud(request);

        List<ItemCompraDTO> items = request.getItems().stream()
                .map(i -> new ItemCompraDTO(i.getProductoId(), i.getCantidad()))
                .collect(Collectors.toList());

        String codigo = (request.getCodigoComprobante() == null || request.getCodigoComprobante().isBlank())
                ? generarCodigoComprobante()
                : request.getCodigoComprobante().trim();

        Venta venta;
        if (request.getModalidadPago() == ModalidadPago.CONTADO) {
            boolean cupon = Boolean.TRUE.equals(request.getCuponValido());
            long descuento = request.getPorcentajeDescuento() == null ? 0 : request.getPorcentajeDescuento();
            venta = registrarVentaContado(codigo, request.getClienteId(), items, cupon, descuento);
        } else {
            long recargo = request.getPorcentajeRecargo() == null ? 0 : request.getPorcentajeRecargo();
            venta = registrarVentaFinanciada(codigo, request.getClienteId(), items, recargo);
        }
        return VentaMapper.toResponse(venta);
    }

    @Transactional(readOnly = true)
    public VentaResponseDTO buscarPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
        return VentaMapper.toResponse(venta);
    }

    /** Historial de ventas de un cliente. */
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> historialPorCliente(Long clienteId) {
        obtenerCliente(clienteId);
        return VentaMapper.toResponseList(ventaRepository.findByClienteId(clienteId));
    }

    /** Estado de cuenta: cuotas ADEUDADAS de un cliente, ordenadas por vencimiento. */
    @Transactional(readOnly = true)
    public EstadoCuentaResponseDTO estadoDeCuenta(Long clienteId) {
        Cliente cliente = obtenerCliente(clienteId);

        List<CuotaResponseDTO> cuotas = cuotaRepository.findByClienteIdAndEstado(clienteId, EstadoCuota.ADEUDADA)
                .stream()
                .sorted((a, b) -> a.getFechaVencimiento().compareTo(b.getFechaVencimiento()))
                .map(VentaMapper::toCuotaResponse)
                .collect(Collectors.toList());

        long total = cuotas.stream().mapToLong(CuotaResponseDTO::getImporteAPagar).sum();

        return new EstadoCuentaResponseDTO(cliente.getId(), VentaMapper.nombreCompleto(cliente),
                cuotas.size(), total, cuotas);
    }

    /** Ventas realizadas entre dos fechas (ambas inclusive). */
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> ventasPorPeriodo(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new SolicitudInvalidaException("Debe indicar las fechas 'desde' y 'hasta'");
        }
        if (desde.isAfter(hasta)) {
            throw new SolicitudInvalidaException("La fecha 'desde' no puede ser posterior a 'hasta'");
        }
        return VentaMapper.toResponseList(ventaRepository.findByFechaBetween(desde, hasta));
    }

    /** Ventas filtradas por modalidad de pago (CONTADO o FINANCIADA). */
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> ventasPorModalidad(ModalidadPago modalidad) {
        if (modalidad == null) {
            throw new SolicitudInvalidaException("Debe indicar la modalidad de pago");
        }
        if (modalidad == ModalidadPago.CONTADO) {
            return VentaMapper.toResponseList(ventaRepository.findAllContado());
        }
        return VentaMapper.toResponseList(ventaRepository.findAllFinanciadas());
    }

    // ---------------------------- auxiliares ----------------------------

    private Cliente obtenerCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + clienteId));
    }

    private String generarCodigoComprobante() {
        return "VTA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void validarSolicitud(VentaRequestDTO r) {
        if (r == null) {
            throw new SolicitudInvalidaException("La solicitud de venta no puede estar vacía");
        }
        if (r.getClienteId() == null) {
            throw new SolicitudInvalidaException("Debe indicar el cliente de la venta");
        }
        if (r.getModalidadPago() == null) {
            throw new SolicitudInvalidaException("Debe indicar la modalidad de pago (CONTADO o FINANCIADA)");
        }
        if (r.getItems() == null || r.getItems().isEmpty()) {
            throw new SolicitudInvalidaException("La venta debe incluir al menos un producto");
        }
        for (ItemVentaRequestDTO item : r.getItems()) {
            if (item == null || item.getProductoId() == null) {
                throw new SolicitudInvalidaException("Cada ítem debe indicar el id del producto");
            }
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new SolicitudInvalidaException(
                        "La cantidad del producto " + item.getProductoId() + " debe ser mayor a 0");
            }
        }
        if (r.getPorcentajeDescuento() != null
                && (r.getPorcentajeDescuento() < 0 || r.getPorcentajeDescuento() > 100)) {
            throw new SolicitudInvalidaException("El porcentaje de descuento debe estar entre 0 y 100");
        }
        if (r.getPorcentajeRecargo() != null && r.getPorcentajeRecargo() < 0) {
            throw new SolicitudInvalidaException("El porcentaje de recargo no puede ser negativo");
        }
    }
    
}