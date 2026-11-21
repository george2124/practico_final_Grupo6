package ar.edu.unju.fi.poo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.unju.fi.poo.dto.CuotaResponseDTO;
import ar.edu.unju.fi.poo.dto.EstadoCuentaResponseDTO;
import ar.edu.unju.fi.poo.enums.EstadoCuota;
import ar.edu.unju.fi.poo.enums.TipoCliente;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.Cuota;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.repository.CuotaRepository;
import ar.edu.unju.fi.poo.repository.VentaRepository;
import ar.edu.unju.fi.poo.service.impl.CuotaServiceImpl;

import ar.edu.unju.fi.poo.exception.SolicitudInvalidaException;
import ar.edu.unju.fi.poo.model.VentaContado;

@ExtendWith(MockitoExtension.class)
class CuotaServiceTest {

    @Mock
    private CuotaRepository cuotaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private CuotaServiceImpl cuotaService;

    private Cliente cliente;
    private VentaFinanciada venta;
    private Cuota cuota1;
    private Cuota cuota2;

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

        venta = new VentaFinanciada();
        venta.setId(10L);
        venta.setCliente(cliente);
        venta.setCodigoComprobante("FC-001");

        cuota1 = new Cuota();
        cuota1.setId(1L);
        cuota1.setNumeroCuota(1);
        cuota1.setImporteAPagar(25000);
        cuota1.setFechaVencimiento(LocalDate.now().minusDays(1));
        cuota1.setEstado(EstadoCuota.ADEUDADA);
        cuota1.setVentaFinanciada(venta);

        cuota2 = new Cuota();
        cuota2.setId(2L);
        cuota2.setNumeroCuota(2);
        cuota2.setImporteAPagar(25000);
        cuota2.setFechaVencimiento(LocalDate.now().plusMonths(1));
        cuota2.setEstado(EstadoCuota.ADEUDADA);
        cuota2.setVentaFinanciada(venta);
    }

    @Test
    void consultarEstadoCuentaExitosamente() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(cuotaRepository.buscarCuotasPorCliente(1L))
                .thenReturn(Arrays.asList(cuota1, cuota2));

        when(ventaRepository.findByClienteId(1L))
                .thenReturn(Collections.emptyList());

        EstadoCuentaResponseDTO resultado =
                cuotaService.obtenerEstadoCuenta(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getClienteId());
        assertEquals(2, resultado.getCantidadCuotasPendientes());
        assertEquals(50000, resultado.getTotalAdeudado());

        assertEquals(EstadoCuota.VENCIDA, cuota1.getEstado());
        assertEquals(EstadoCuota.ADEUDADA, cuota2.getEstado());

        verify(cuotaRepository).saveAll(anyList());
    }

    @Test
    void cobrarCuotaVencidaExitosamente() {

        when(ventaRepository.findById(10L))
                .thenReturn(Optional.of(venta));

        when(cuotaRepository.buscarCuotasPorVenta(10L))
                .thenReturn(Arrays.asList(cuota1, cuota2));

        when(cuotaRepository.save(cuota1))
                .thenReturn(cuota1);

        when(ventaRepository.save(venta))
                .thenReturn(venta);

        CuotaResponseDTO resultado =
                cuotaService.cobrarSiguienteCuota(10L);

        assertNotNull(resultado);
        assertEquals(1, resultado.getNumeroCuota());
        assertEquals(EstadoCuota.PAGADA, cuota1.getEstado());
        assertEquals(EstadoCuota.ADEUDADA, cuota2.getEstado());

        assertFalse(venta.isSaldada());

        verify(cuotaRepository).save(cuota1);
        verify(ventaRepository).save(venta);
    }

    @Test
    void cobrarCuotaCorrelativaEnFechaPreviaExitosamente() {

        cuota1.setFechaVencimiento(LocalDate.now().plusDays(5));

        when(ventaRepository.findById(10L))
                .thenReturn(Optional.of(venta));

        when(cuotaRepository.buscarCuotasPorVenta(10L))
                .thenReturn(Arrays.asList(cuota1, cuota2));

        when(cuotaRepository.save(cuota1))
                .thenReturn(cuota1);

        when(ventaRepository.save(venta))
                .thenReturn(venta);

        CuotaResponseDTO resultado =
                cuotaService.cobrarSiguienteCuota(10L);

        assertNotNull(resultado);
        assertEquals(1, resultado.getNumeroCuota());
        assertEquals(25000, cuota1.getImporteAPagar());
        assertEquals(EstadoCuota.PAGADA, cuota1.getEstado());
        assertEquals(EstadoCuota.ADEUDADA, cuota2.getEstado());
        assertFalse(venta.isSaldada());

        verify(cuotaRepository).save(cuota1);
        verify(ventaRepository).save(venta);
    }

    @Test
    void marcarVentaComoSaldadaAlPagarUltimaCuota() {

        when(ventaRepository.findById(10L))
                .thenReturn(Optional.of(venta));

        when(cuotaRepository.buscarCuotasPorVenta(10L))
                .thenReturn(Collections.singletonList(cuota1));

        when(cuotaRepository.save(cuota1))
                .thenReturn(cuota1);

        when(ventaRepository.save(venta))
                .thenReturn(venta);

        cuotaService.cobrarSiguienteCuota(10L);

        assertEquals(EstadoCuota.PAGADA, cuota1.getEstado());
        assertTrue(venta.isSaldada());
    }
    

	@Test
	void cobrarVentaSinCuotasRegistradasLanzaExcepcion() {
	
	    when(ventaRepository.findById(10L))
	        .thenReturn(Optional.of(venta));
	
	    when(cuotaRepository.buscarCuotasPorVenta(10L))
	        .thenReturn(Collections.emptyList());
	
	    assertThrows(
	        SolicitudInvalidaException.class,
	        () -> cuotaService.cobrarSiguienteCuota(10L)
	    );
	
	    verify(cuotaRepository, never()).save(any(Cuota.class));
	    verify(ventaRepository, never()).save(any());
	}
	
	@Test
	void cobrarVentaConTodasLasCuotasPagadasLanzaExcepcion() {
	
	    cuota1.setEstado(EstadoCuota.PAGADA);
	
	    when(ventaRepository.findById(10L))
	        .thenReturn(Optional.of(venta));
	
	    when(cuotaRepository.buscarCuotasPorVenta(10L))
	        .thenReturn(Collections.singletonList(cuota1));
	
	    assertThrows(
	        SolicitudInvalidaException.class,
	        () -> cuotaService.cobrarSiguienteCuota(10L)
	    );
	
	    verify(cuotaRepository, never()).save(any(Cuota.class));
	    verify(ventaRepository, never()).save(any());
	}
	
	@Test
	void cobrarVentaDeContadoLanzaExcepcion() {
	
	    VentaContado ventaContado = new VentaContado();
	    ventaContado.setId(20L);
	
	    when(ventaRepository.findById(20L))
	        .thenReturn(Optional.of(ventaContado));
	
	    assertThrows(
	        SolicitudInvalidaException.class,
	        () -> cuotaService.cobrarSiguienteCuota(20L)
	    );
	
	    verify(cuotaRepository, never()).buscarCuotasPorVenta(any());
	    verify(cuotaRepository, never()).save(any(Cuota.class));
	}
}