package ar.edu.unju.fi.poo.controller;

import java.net.URI;
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

import ar.edu.unju.fi.poo.dto.EstadoCuentaResponseDTO;
import ar.edu.unju.fi.poo.dto.VentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;
import ar.edu.unju.fi.poo.enums.ModalidadPago;
import ar.edu.unju.fi.poo.service.VentaService;

/**
 * Controlador REST de ventas. Solo recibe/devuelve DTOs: nunca entidades.
 * Los errores los resuelve el GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/ventas")
public class VentaController {

	@Autowired
	private VentaService ventaService;

	// Registrar venta (contado o financiada)
	@PostMapping
	public ResponseEntity<VentaResponseDTO> registrar(@RequestBody VentaRequestDTO request) {
		VentaResponseDTO creada = ventaService.registrarVenta(request);
		return ResponseEntity.created(URI.create("/api/ventas/" + creada.getId())).body(creada);
	}

	// Buscar una venta por id
	@GetMapping("/{id}")
	public ResponseEntity<VentaResponseDTO> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(ventaService.buscarPorId(id));
	}

	// Historial de ventas de un cliente
	@GetMapping("/cliente/{clienteId}")
	public ResponseEntity<List<VentaResponseDTO>> historialPorCliente(@PathVariable Long clienteId) {
		return ResponseEntity.ok(ventaService.historialPorCliente(clienteId));
	}

	// Estado de cuenta: cuotas pendientes de un cliente
	@GetMapping("/cliente/{clienteId}/cuotas-pendientes")
	public ResponseEntity<EstadoCuentaResponseDTO> estadoDeCuenta(@PathVariable Long clienteId) {
		return ResponseEntity.ok(ventaService.estadoDeCuenta(clienteId));
	}

	// Ventas dentro de un período. Ej: /api/ventas/periodo?desde=2026-01-01&hasta=2026-12-31
	@GetMapping("/periodo")
	public ResponseEntity<List<VentaResponseDTO>> ventasPorPeriodo(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		return ResponseEntity.ok(ventaService.ventasPorPeriodo(desde, hasta));
	}

	// Filtrar por modalidad de pago. Ej: /api/ventas/modalidad/CONTADO
	@GetMapping("/modalidad/{modalidad}")
	public ResponseEntity<List<VentaResponseDTO>> ventasPorModalidad(@PathVariable ModalidadPago modalidad) {
		return ResponseEntity.ok(ventaService.ventasPorModalidad(modalidad));
	}
}
