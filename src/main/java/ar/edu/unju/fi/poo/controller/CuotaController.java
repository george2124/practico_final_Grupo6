package ar.edu.unju.fi.controller; // <- ajustá este package al que use tu proyecto

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Con Ctrl + Shift + O Eclipse importa automáticamente CuotaService y los DTOs

@RestController
@RequestMapping("/api/cuotas")
public class CuotaController {

    @Autowired
    private CuotaService cuotaService;

    // GET: Consultar estado de cuenta de un cliente
    @GetMapping("/estado-cuenta/{clienteId}")
    public ResponseEntity<EstadoCuentaResponseDTO> obtenerEstadoCuenta(@PathVariable Long clienteId) {
        return ResponseEntity.ok(cuotaService.obtenerEstadoCuenta(clienteId));
    }

    // POST: Cobrar la siguiente cuota pendiente de una venta financiada
    @PostMapping("/cobrar/{ventaId}")
    public ResponseEntity<CuotaResponseDTO> cobrarSiguienteCuota(@PathVariable Long ventaId) {
        return ResponseEntity.ok(cuotaService.cobrarSiguienteCuota(ventaId));
    }
}