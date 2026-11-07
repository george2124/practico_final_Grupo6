package ar.edu.unju.fi.poo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.unju.fi.poo.enums.TipoCliente;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(
            1L,
            "12345678",
            "Juan",
            "Perez",
            "3884123456",
            "juan@mail.com",
            "San Pedro",
            TipoCliente.PARTICULAR
        );
    }

    @Test
    void guardarClienteExitosamente() {
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.guardarCliente(cliente);

        assertNotNull(resultado);
        assertEquals("12345678", resultado.getIdentificacion());
        assertEquals("Juan", resultado.getNombre());

        verify(clienteRepository).save(cliente);
    }

    @Test
    void buscarClientePorIdentificacionExitosamente() {
        when(clienteRepository.findByIdentificacion("12345678"))
            .thenReturn(Optional.of(cliente));

        Cliente resultado =
            clienteService.buscarPorIdentificacion("12345678");

        assertNotNull(resultado);
        assertEquals("12345678", resultado.getIdentificacion());
    }

    @Test
    void buscarClienteInexistenteLanzaExcepcion() {
        when(clienteRepository.findByIdentificacion("99999999"))
            .thenReturn(Optional.empty());

        assertThrows(
            RecursoNoEncontradoException.class,
            () -> clienteService.buscarPorIdentificacion("99999999")
        );
    }

    @Test
    void buscarClientePorNombreORazonSocial() {
        when(clienteRepository
            .findByNombreContainingIgnoreCaseOrApellidoRazonSocialContainingIgnoreCase(
                "Juan", "Juan"
            )).thenReturn(Arrays.asList(cliente));

        List<Cliente> resultado =
            clienteService.buscarPorNombreOrRazonSocial("Juan");

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void listarTodosLosClientes() {
        when(clienteRepository.findAll())
            .thenReturn(Arrays.asList(cliente));

        List<Cliente> resultado = clienteService.listarTodos();

        assertEquals(1, resultado.size());
        verify(clienteRepository).findAll();
    }
}
