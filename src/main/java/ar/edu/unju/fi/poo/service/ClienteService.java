package ar.edu.unju.fi.poo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorIdentificacion(String identificacion) {
        return clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con identificación: " + identificacion));
    }

    public List<Cliente> buscarPorNombreOrRazonSocial(String termino) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidoRazonSocialContainingIgnoreCase(termino, termino);
    }
    
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
}