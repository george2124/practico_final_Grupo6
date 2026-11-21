package ar.edu.unju.fi.poo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

import ar.edu.unju.fi.poo.dto.ClienteRequestDTO;
import ar.edu.unju.fi.poo.dto.ClienteResponseDTO;
import ar.edu.unju.fi.poo.enums.TipoCliente;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.service.impl.ClienteServiceImpl;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteRequestDTO requestDTO;

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

        requestDTO = new ClienteRequestDTO();

        requestDTO.setIdentificador("12345678");
        requestDTO.setNombre("Juan");
        requestDTO.setApellidoRazonSocial("Perez");
        requestDTO.setTelefono("3884123456");
        requestDTO.setEmail("juan@mail.com");
        requestDTO.setDireccion("San Pedro");
        requestDTO.setTipoCliente(TipoCliente.PARTICULAR);
    }

    @Test
    void guardarClienteExitosamente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponseDTO resultado = clienteService.guardarCliente(requestDTO);

        assertNotNull(resultado);
        assertEquals("12345678", resultado.getIdentificador());
        assertEquals("Juan", resultado.getNombre());

        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void actualizarClienteExitosamente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponseDTO resultado = clienteService.actualizarCliente(1L, requestDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());

        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void buscarClientePorIdentificacionExitosamente() {
        when(clienteRepository.findByIdentificacion("12345678"))
            .thenReturn(Optional.of(cliente));

        ClienteResponseDTO resultado = clienteService.buscarPorIdentificacion("12345678");

        assertNotNull(resultado);
        assertEquals("12345678", resultado.getIdentificador());
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

        List<ClienteResponseDTO> resultado = clienteService.buscarPorNombreOrRazonSocial("Juan");

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
    }

    @Test
    void listarTodosLosClientes() {
        when(clienteRepository.findAll())
            .thenReturn(Arrays.asList(cliente));

        List<ClienteResponseDTO> resultado = clienteService.listarTodos();

        assertEquals(1, resultado.size());
        verify(clienteRepository).findAll();
    }
    

	@Test
	void actualizarClienteInexistenteLanzaExcepcion() {
	
	    when(clienteRepository.findById(99L))
	        .thenReturn(Optional.empty());
	
	    assertThrows(
	        RecursoNoEncontradoException.class,
	        () -> clienteService.actualizarCliente(99L, requestDTO)
	    );
	
	    verify(clienteRepository).findById(99L);
	    verify(clienteRepository, never()).save(any(Cliente.class));
	}
	
	@Test
	void buscarClientesSinResultadosDevuelveListaVacia() {
	
	    when(clienteRepository
	        .findByNombreContainingIgnoreCaseOrApellidoRazonSocialContainingIgnoreCase(
	            "Inexistente", "Inexistente"
	        )).thenReturn(Arrays.asList());
	
	    List<ClienteResponseDTO> resultado =
	        clienteService.buscarPorNombreOrRazonSocial("Inexistente");
	
	    assertNotNull(resultado);
	    assertTrue(resultado.isEmpty());
	
	    verify(clienteRepository).findByNombreContainingIgnoreCaseOrApellidoRazonSocialContainingIgnoreCase(
	        "Inexistente", "Inexistente"
	    );
	}
}