package ar.edu.unju.fi.poo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unju.fi.poo.dto.ClienteRequestDTO;
import ar.edu.unju.fi.poo.dto.ClienteResponseDTO;
import ar.edu.unju.fi.poo.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Crear Cliente (POST)
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(@RequestBody ClienteRequestDTO request) {
        ClienteResponseDTO creado = clienteService.guardarCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Buscar por DNI o CUIT (GET)
    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<ClienteResponseDTO> buscarPorIdentificacion(@PathVariable String identificacion) {
        return ResponseEntity.ok(clienteService.buscarPorIdentificacion(identificacion));
    }

    // Buscar por Nombre o Razón Social (GET)
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNombreOrRazonSocial(@RequestParam String termino) {
        return ResponseEntity.ok(clienteService.buscarPorNombreOrRazonSocial(termino));
    }

    // Listar todos los clientes (GET)
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }
}