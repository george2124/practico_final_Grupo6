package ar.edu.unju.fi.poo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarPorIdentificacion(String identificacion) {
        return clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con identificación: " + identificacion));
    }

    @Override
    public List<Cliente> buscarPorNombreOrRazonSocial(String termino) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidoRazonSocialContainingIgnoreCase(termino, termino);
    }
    
    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
}