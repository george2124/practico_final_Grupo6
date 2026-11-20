package ar.edu.unju.fi.poo.service;

import java.util.List;
<<<<<<< HEAD
import ar.edu.unju.fi.poo.model.Cliente;
=======

import ar.edu.unju.fi.poo.dto.ClienteRequestDTO;
import ar.edu.unju.fi.poo.dto.ClienteResponseDTO;

public interface ClienteService {
>>>>>>> refs/remotes/origin/fix/desacoplamiento-dto

<<<<<<< HEAD
public interface ClienteService {
    
    Cliente guardarCliente(Cliente cliente);
    
    Cliente buscarPorIdentificacion(String identificacion);
    
    List<Cliente> buscarPorNombreOrRazonSocial(String termino);
    
    List<Cliente> listarTodos();
    
=======
    ClienteResponseDTO guardarCliente(ClienteRequestDTO request);

    ClienteResponseDTO buscarPorIdentificacion(String identificacion);

    List<ClienteResponseDTO> buscarPorNombreOrRazonSocial(String termino);

    List<ClienteResponseDTO> listarTodos();

>>>>>>> refs/remotes/origin/fix/desacoplamiento-dto
}