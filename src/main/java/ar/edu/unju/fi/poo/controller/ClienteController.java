package ar.edu.unju.fi.poo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	// Crear Cliente (POST)
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(@RequestBody ClienteRequestDTO request) {
        // Convertir DTO de entrada a Entidad para dárselo al Service
        Cliente clienteANatativo = new Cliente(
            null, // El ID se genera de forma autoincremental en la base de datos
            request.getIdentificador(),
            request.getNombre(),
            request.getApellidoRazonSocial(),
            request.getTelefono(),
            request.getEmail(),
            request.getDireccion(),
            request.getTipoCliente()
        );

        Cliente clienteGuardado = clienteService.guardarCliente(clienteANatativo);
        
        // Convertir la Entidad resultante a DTO de salida
        return ResponseEntity.ok(convertirA_ResponseDTO(clienteGuardado));
    }

    // Buscar por DNI o CUIT (GET)
    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<ClienteResponseDTO> buscarPorIdentificacion(@PathVariable String identificacion) {
        Cliente cliente = clienteService.buscarPorIdentificacion(identificacion);
        return ResponseEntity.ok(convertirA_ResponseDTO(cliente));
    }

    // Buscar por Nombre o Razón Social (GET)
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNombreOrRazonSocial(@RequestParam String termino) {
        List<Cliente> clientes = clienteService.buscarPorNombreOrRazonSocial(termino);
        
        // Mapear la lista de entidades a una lista de DTOs
        List<ClienteResponseDTO> dtos = clientes.stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(dtos);
    }

    // Listar todos los clientes (GET)
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        
        List<ClienteResponseDTO> dtos = clientes.stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(dtos);
    }

    // Método auxiliar privado para pasar de Entidad a DTO de respuesta
    private ClienteResponseDTO convertirA_ResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
            cliente.getId(),
            cliente.getIdentificacion(),
            cliente.getNombre(),
            cliente.getApellidoRazonSocial(),
            cliente.getTelefono(),
            cliente.getEmail(),
            cliente.getDireccion(),
            cliente.getTipoCliente()
        );
    }
}
