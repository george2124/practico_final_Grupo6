package ar.edu.unju.fi.poo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unju.fi.poo.dto.CuotaResponseDTO;
import ar.edu.unju.fi.poo.dto.EstadoCuentaResponseDTO;
import ar.edu.unju.fi.poo.service.CuotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cuotas", description = "Consulta de estado de cuenta y cobro de cuotas de ventas financiadas")
@RestController
@RequestMapping("/api/cuotas")
public class CuotaController {

    private final CuotaService cuotaService;

    public CuotaController(CuotaService cuotaService) {
        this.cuotaService = cuotaService;
    }

    @Operation(
        summary = "Consultar estado de cuenta del cliente",
        description = "Actualiza cuotas vencidas, devuelve las cuotas pendientes, el total adeudado y el historial de ventas."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado de cuenta obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/estado-cuenta/{clienteId}")
    public ResponseEntity<EstadoCuentaResponseDTO> obtenerEstadoCuenta(@PathVariable Long clienteId) {
        return ResponseEntity.ok(cuotaService.obtenerEstadoCuenta(clienteId));
    }

    @Operation(
        summary = "Cobrar siguiente cuota",
        description = "Registra el pago de la siguiente cuota correlativa pendiente de una venta financiada."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cuota cobrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida o cuota con vencimiento futuro"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @PostMapping("/cobrar/{ventaId}")
    public ResponseEntity<CuotaResponseDTO> cobrarSiguienteCuota(@PathVariable Long ventaId) {
        return ResponseEntity.ok(cuotaService.cobrarSiguienteCuota(ventaId));
    }
}