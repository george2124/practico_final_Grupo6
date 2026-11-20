package ar.edu.unju.fi.poo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.dto.ClienteRequestDTO;
import ar.edu.unju.fi.poo.dto.ClienteResponseDTO;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public ClienteResponseDTO guardarCliente(ClienteRequestDTO request) {
        Cliente cliente = new Cliente(
                null,
                request.getIdentificador(),
                request.getNombre(),
                request.getApellidoRazonSocial(),
                request.getTelefono(),
                request.getEmail(),
                request.getDireccion(),
                request.getTipoCliente()
        );
        Cliente guardado = clienteRepository.save(cliente);
        return mapearAResponseDTO(guardado);
    }

    @Override
    public ClienteResponseDTO buscarPorIdentificacion(String identificacion) {
        Cliente cliente = clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado con identificación: " + identificacion));
        return mapearAResponseDTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> buscarPorNombreOrRazonSocial(String termino) {
        return clienteRepository
                .findByNombreContainingIgnoreCaseOrApellidoRazonSocialContainingIgnoreCase(termino, termino)
                .stream()
                .map(this::mapearAResponseDTO)
                .toList();
    }

    @Override
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::mapearAResponseDTO)
                .toList();
    }

    private ClienteResponseDTO mapearAResponseDTO(Cliente cliente) {
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