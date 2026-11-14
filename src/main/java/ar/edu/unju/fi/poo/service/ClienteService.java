package ar.edu.unju.fi.poo.service;

import java.util.List;
import ar.edu.unju.fi.poo.model.Cliente;

public interface ClienteService {
    
    Cliente guardarCliente(Cliente cliente);
    
    Cliente buscarPorIdentificacion(String identificacion);
    
    List<Cliente> buscarPorNombreOrRazonSocial(String termino);
    
    List<Cliente> listarTodos();
    
}