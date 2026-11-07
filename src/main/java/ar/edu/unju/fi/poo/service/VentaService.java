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

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

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
}