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
}