package ar.edu.unju.fi.poo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unju.fi.poo.dto.VentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;
import ar.edu.unju.fi.poo.enums.ModalidadPago;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.DetalleVenta;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // Registrar Venta de Contado (POST)
    @PostMapping("/contado")
    public ResponseEntity<VentaResponseDTO> registrarVentaContado(@RequestBody VentaRequestDTO request) {
        VentaContado venta = new VentaContado();

        Cliente cliente = new Cliente();
        cliente.setId(request.getClienteId());
        venta.setCliente(cliente);
        venta.setFecha(LocalDate.now());
        venta.setCodigoComprobante(request.getCodigoComprobante());

        if (request.getItems() != null) {
            List<DetalleVenta> detalles = request.getItems().stream().map(item -> {
                DetalleVenta detalle = new DetalleVenta();
                Producto producto = new Producto();
                producto.setId(item.getProductoId());
                detalle.setProducto(producto);
                detalle.setCantidad(item.getCantidad());
                detalle.setVenta(venta);
                return detalle;
            }).collect(Collectors.toList());
            venta.setDetalles(detalles);
        }

        VentaContado ventaGuardada = ventaService.registrarVentaContado(venta);
        return ResponseEntity.ok(convertirA_ResponseDTO(ventaGuardada));
    }

    // Registrar Venta Financiada (POST)
    @PostMapping("/financiada")
    public ResponseEntity<VentaResponseDTO> registrarVentaFinanciada(@RequestBody VentaRequestDTO request) {
        VentaFinanciada venta = new VentaFinanciada();

        Cliente cliente = new Cliente();
        cliente.setId(request.getClienteId());
        venta.setCliente(cliente);
        venta.setFecha(LocalDate.now());
        venta.setCodigoComprobante(request.getCodigoComprobante());

        if (request.getItems() != null) {
            List<DetalleVenta> detalles = request.getItems().stream().map(item -> {
                DetalleVenta detalle = new DetalleVenta();
                Producto producto = new Producto();
                producto.setId(item.getProductoId());
                detalle.setProducto(producto);
                detalle.setCantidad(item.getCantidad());
                detalle.setVenta(venta);
                return detalle;
            }).collect(Collectors.toList());
            venta.setDetalles(detalles);
        }

        VentaFinanciada ventaGuardada = ventaService.registrarVentaFinanciada(venta);
        return ResponseEntity.ok(convertirA_ResponseDTO(ventaGuardada));
    }

    // Listar todas las ventas (GET)
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarTodas() {
        List<Venta> ventas = ventaService.listarTodas();
        List<VentaResponseDTO> dtos = ventas.stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Consultar historial de ventas por cliente (GET)
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VentaResponseDTO>> consultarVentasPorCliente(@PathVariable Long clienteId) {
        List<Venta> ventas = ventaService.consultarVentasPorCliente(clienteId);
        List<VentaResponseDTO> dtos = ventas.stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Consultar ventas por rango de fechas (GET)
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<VentaResponseDTO>> consultarVentasPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<Venta> ventas = ventaService.consultarVentasPorRangoFechas(fechaInicio, fechaFin);
        List<VentaResponseDTO> dtos = ventas.stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Filtrar ventas por modalidad (GET)
    @GetMapping("/modalidad")
    public ResponseEntity<List<VentaResponseDTO>> filtrarPorModalidad(@RequestParam String modalidad) {
        List<Venta> ventas = ventaService.filtrarPorModalidad(modalidad);
        List<VentaResponseDTO> dtos = ventas.stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Método auxiliar privado para pasar de Entidad a VentaResponseDTO usando sus setters
    private VentaResponseDTO convertirA_ResponseDTO(Venta venta) {
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
}