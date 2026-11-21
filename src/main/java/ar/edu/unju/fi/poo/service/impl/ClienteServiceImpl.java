package ar.edu.unju.fi.poo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
    @Transactional
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id));

        cliente.setIdentificacion(request.getIdentificador());
        cliente.setNombre(request.getNombre());
        cliente.setApellidoRazonSocial(request.getApellidoRazonSocial());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setDireccion(request.getDireccion());
        cliente.setTipoCliente(request.getTipoCliente());

        Cliente actualizado = clienteRepository.save(cliente);
        return mapearAResponseDTO(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorIdentificacion(String identificacion) {
        Cliente cliente = clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado con identificación: " + identificacion));
        return mapearAResponseDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarPorNombreOrRazonSocial(String termino) {
        return clienteRepository
                .findByNombreContainingIgnoreCaseOrApellidoRazonSocialContainingIgnoreCase(termino, termino)
                .stream()
                .map(this::mapearAResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::mapearAResponseDTO)
                .toList();
    }

    private ClienteResponseDTO mapearAResponseDTO(Cliente cliente) {

        ClienteResponseDTO response = new ClienteResponseDTO();

        response.setId(cliente.getId());
        response.setIdentificador(cliente.getIdentificacion());
        response.setNombre(cliente.getNombre());
        response.setApellidoRazonSocial(cliente.getApellidoRazonSocial());
        response.setTelefono(cliente.getTelefono());
        response.setEmail(cliente.getEmail());
        response.setDireccion(cliente.getDireccion());
        response.setTipoCliente(cliente.getTipoCliente());

        return response;
    }
}