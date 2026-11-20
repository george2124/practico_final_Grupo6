package ar.edu.unju.fi.poo.controller;

import java.time.LocalDate;
import java.util.List;

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
import ar.edu.unju.fi.poo.mapper.VentaMapper;
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ventas", description = "Registro y consulta de ventas de contado y financiadas")
@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Operation(
        summary = "Registrar venta de contado",
        description = "Registra una nueva venta de contado a partir de un cliente y sus ítems."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta de contado registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente")
    })
    @PostMapping("/contado")
    public ResponseEntity<VentaResponseDTO> registrarVentaContado(@RequestBody VentaRequestDTO request) {
        VentaContado venta = VentaMapper.toVentaContado(request);
        VentaContado ventaGuardada = ventaService.registrarVentaContado(venta);
        return ResponseEntity.ok(VentaMapper.toResponse(ventaGuardada));
    }

    @Operation(
        summary = "Registrar venta financiada",
        description = "Registra una nueva venta financiada y genera su plan de cuotas."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta financiada registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente")
    })
    @PostMapping("/financiada")
    public ResponseEntity<VentaResponseDTO> registrarVentaFinanciada(@RequestBody VentaRequestDTO request) {
        VentaFinanciada venta = VentaMapper.toVentaFinanciada(request);
        VentaFinanciada ventaGuardada = ventaService.registrarVentaFinanciada(venta);
        return ResponseEntity.ok(VentaMapper.toResponse(ventaGuardada));
    }

    @Operation(
        summary = "Listar todas las ventas",
        description = "Devuelve el listado completo de ventas registradas."
    )
    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarTodas() {
        List<Venta> ventas = ventaService.listarTodas();
        return ResponseEntity.ok(VentaMapper.toResponseList(ventas));
    }

    @Operation(
        summary = "Consultar ventas por cliente",
        description = "Obtiene el historial de ventas asociadas a un cliente por su ID."
    )
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VentaResponseDTO>> consultarVentasPorCliente(@PathVariable Long clienteId) {
        List<Venta> ventas = ventaService.consultarVentasPorCliente(clienteId);
        return ResponseEntity.ok(VentaMapper.toResponseList(ventas));
    }

    @Operation(
        summary = "Consultar ventas por rango de fechas",
        description = "Filtra las ventas registradas entre una fecha de inicio y una fecha de fin."
    )
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<VentaResponseDTO>> consultarVentasPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Venta> ventas = ventaService.consultarVentasPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(VentaMapper.toResponseList(ventas));
    }

    @Operation(
        summary = "Filtrar ventas por modalidad",
        description = "Filtra las ventas según su modalidad de pago (CONTADO o FINANCIADA)."
    )
    @GetMapping("/modalidad")
    public ResponseEntity<List<VentaResponseDTO>> filtrarPorModalidad(@RequestParam String modalidad) {
        List<Venta> ventas = ventaService.filtrarPorModalidad(modalidad);
        return ResponseEntity.ok(VentaMapper.toResponseList(ventas));
    }
}