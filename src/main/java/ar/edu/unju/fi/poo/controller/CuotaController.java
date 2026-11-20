package ar.edu.unju.fi.poo.controller;

<<<<<<< HEAD
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unju.fi.poo.dto.CuotaResponseDTO;
import ar.edu.unju.fi.poo.dto.EstadoCuentaResponseDTO;
import ar.edu.unju.fi.poo.service.CuotaService;

@RestController
@RequestMapping("/api/cuotas")
public class CuotaController {

    private final CuotaService cuotaService;

    public CuotaController(CuotaService cuotaService) {
        this.cuotaService = cuotaService;
    }

    // Consultar estado de cuenta de un cliente.
    @GetMapping("/estado-cuenta/{clienteId}")
    public ResponseEntity<EstadoCuentaResponseDTO> obtenerEstadoCuenta(
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(
                cuotaService.obtenerEstadoCuenta(clienteId));
    }

    // Cobrar la siguiente cuota correlativa de una venta.
    @PostMapping("/cobrar/{ventaId}")
    public ResponseEntity<CuotaResponseDTO> cobrarSiguienteCuota(
            @PathVariable Long ventaId) {

        return ResponseEntity.ok(
                cuotaService.cobrarSiguienteCuota(ventaId));
    }
}
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unju.fi.poo.dto.CuotaResponseDTO;
import ar.edu.unju.fi.poo.dto.EstadoCuentaResponseDTO;
import ar.edu.unju.fi.poo.service.CuotaService;

@RestController
@RequestMapping("/api/cuotas")
public class CuotaController {

    @Autowired
    private CuotaService cuotaService;

    @GetMapping("/estado-cuenta/{clienteId}")
    public ResponseEntity<EstadoCuentaResponseDTO> obtenerEstadoCuenta(@PathVariable Long clienteId) {
        return ResponseEntity.ok(cuotaService.obtenerEstadoCuenta(clienteId));
    }

    @PostMapping("/cobrar/{ventaId}")
    public ResponseEntity<CuotaResponseDTO> cobrarSiguienteCuota(@PathVariable Long ventaId) {
        return ResponseEntity.ok(cuotaService.cobrarSiguienteCuota(ventaId));
    }
}
>>>>>>> refs/remotes/origin/fix/desacoplamiento-dto
