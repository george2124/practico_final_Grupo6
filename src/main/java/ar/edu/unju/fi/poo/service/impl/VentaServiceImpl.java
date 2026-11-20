<<<<<<< HEAD
package ar.edu.unju.fi.poo.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.repository.VentaRepository;
import ar.edu.unju.fi.poo.service.VentaService;
import jakarta.transaction.Transactional;
import ar.edu.unju.fi.poo.service.ProductoService;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private ProductoService productoService;

    @Override
    @Transactional
    public VentaContado registrarVentaContado(VentaContado ventaContado) {
    	//1. Modularización: Validar que la venta contenga elementos
    	validarEstructuraVenta(ventaContado);
    	
    	//2. Modularización: Processar fecha de emisión si no viene asignada
    	procesarFechaVenta(ventaContado);
    	
    	// 3. Modularización: Procesar stock y cálculos de la venta de contado
        procesarDetallesYStock(ventaContado);

        // 4. Guardado final en la base de datos
        return ventaRepository.save(ventaContado);
    }
    
    @Override
    @Transactional
    public VentaFinanciada registrarVentaFinanciada(VentaFinanciada ventaFinanciada) {
        // 1. Modularización: Validar que la venta contenga elementos
        validarEstructuraVenta(ventaFinanciada);
        
        // 2. Modularización: Procesar fecha de emisión
        procesarFechaVenta(ventaFinanciada);
        
        // 3. Modularización: Procesar stock y cálculos específicos de financiación
        procesarDetallesYStock(ventaFinanciada);

        // 4. Guardado final en la base de datos
        return ventaRepository.save(ventaFinanciada);
    }

    // --- MÉTODOS PRIVADOS DE MODULARIZACIÓN (Punto 4) ---
    private void validarEstructuraVenta(Venta venta) {
        if (venta.getCliente() == null) {
            throw new IllegalArgumentException("La venta debe estar asociada a un cliente válido.");
        }
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe registrar al menos un detalle de producto.");
        }
    }

    private void procesarFechaVenta(Venta venta) {
        if (venta.getFecha() == null) {
            venta.setFecha(LocalDate.now());
        }
    }

    private void procesarDetallesYStock(Venta venta) {
        // Aquí puedes modularizar la iteración sobre los detalles de la venta,
        // validar que hay stock suficiente en cada producto y descontarlo llamando a productoService.
        venta.getDetalles().forEach(detalle -> {
            // Ejemplo de lógica modularizada:
            // productoService.descontarStock(detalle.getProducto().getId(), detalle.getCantidad());
        });
    }

    // --- MÉTODOS DE CONSULTA EXISTENTES ---
    @Override
    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    @Override
    public List<Venta> consultarVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Venta> consultarVentasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Venta> filtrarPorModalidad(String modalidad) {
        if ("CONTADO".equalsIgnoreCase(modalidad)) {
            return ventaRepository.findByModalidadPago(VentaContado.class);
        } else if ("FINANCIADA".equalsIgnoreCase(modalidad)) {
            return ventaRepository.findByModalidadPago(VentaFinanciada.class);
        }
        return ventaRepository.findAll(); 
    }
=======
package ar.edu.unju.fi.poo.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.poo.dto.VentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;
import ar.edu.unju.fi.poo.enums.EstadoCuota;
import ar.edu.unju.fi.poo.enums.ModalidadPago;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.exception.StockInsuficienteException;
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
        return convertirAResponseDTO(guardada);
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

        return convertirAResponseDTO(ventaRepository.save(ventaGuardada));
    }

    @Override
    public List<VentaResponseDTO> listarTodas() {
        return ventaRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VentaResponseDTO> consultarVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VentaResponseDTO> consultarVentasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VentaResponseDTO> filtrarPorModalidad(String modalidad) {
        return ventaRepository.findAll().stream()
                .filter(v -> {
                    if ("CONTADO".equalsIgnoreCase(modalidad)) {
                        return v instanceof VentaContado;
                    }
                    if ("FINANCIADA".equalsIgnoreCase(modalidad)) {
                        return v instanceof VentaFinanciada;
                    }
                    return true;
                })
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
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

    private VentaResponseDTO convertirAResponseDTO(Venta venta) {
        VentaResponseDTO response = new VentaResponseDTO();
        response.setId(venta.getId());
        response.setCodigoComprobante(venta.getCodigoComprobante());
        response.setFecha(venta.getFecha());

        if (venta.getCliente() != null) {
            response.setClienteId(venta.getCliente().getId());
            response.setClienteNombre(venta.getCliente().getNombre() + " " + venta.getCliente().getApellidoRazonSocial());
        }

        if (venta instanceof VentaContado) {
            response.setModalidadPago(ModalidadPago.CONTADO);
        } else if (venta instanceof VentaFinanciada) {
            response.setModalidadPago(ModalidadPago.FINANCIADA);
        }

        return response;
    }
>>>>>>> refs/remotes/origin/fix/desacoplamiento-dto
}