package ar.edu.unju.fi.poo.service;

import java.util.List;

import ar.edu.unju.fi.poo.dto.ClienteRequestDTO;
import ar.edu.unju.fi.poo.dto.ClienteResponseDTO;

public interface ClienteService {

    ClienteResponseDTO guardarCliente(ClienteRequestDTO request);

    ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO request);

    ClienteResponseDTO buscarPorIdentificacion(String identificacion);

    List<ClienteResponseDTO> buscarPorNombreOrRazonSocial(String termino);

    List<ClienteResponseDTO> listarTodos();
}