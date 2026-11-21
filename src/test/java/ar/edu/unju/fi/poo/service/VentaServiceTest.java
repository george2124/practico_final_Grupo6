package ar.edu.unju.fi.poo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.unju.fi.poo.dto.ItemVentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;
import ar.edu.unju.fi.poo.enums.ModalidadPago;
import ar.edu.unju.fi.poo.enums.TipoCliente;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.DetalleVenta;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.repository.ProductoRepository;
import ar.edu.unju.fi.poo.repository.VentaRepository;
import ar.edu.unju.fi.poo.service.impl.VentaServiceImpl;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.exception.SolicitudInvalidaException;
import ar.edu.unju.fi.poo.exception.StockInsuficienteException;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private VentaContado ventaContado;
    private VentaFinanciada ventaFinanciada;
    private Cliente cliente;
    private Producto producto;
    private VentaRequestDTO requestContado;
    private VentaRequestDTO requestFinanciada;

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

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Producto para pruebas");
        producto.setMarca("Marca");
        producto.setStock(10);
        producto.setPrecioBase(1000L);

        ItemVentaRequestDTO item = new ItemVentaRequestDTO(1L, 1);

        requestContado = new VentaRequestDTO(
            1L,
            ModalidadPago.CONTADO,
            "COMP-001",
            List.of(item),
            false,
            0L,
            0L
        );

        requestFinanciada = new VentaRequestDTO(
            1L,
            ModalidadPago.FINANCIADA,
            "COMP-002",
            List.of(item),
            false,
            0L,
            10L
        );

        ventaContado = new VentaContado();
        ventaContado.setId(1L);
        ventaContado.setCodigoComprobante("COMP-001");
        ventaContado.setFecha(LocalDate.now());
        ventaContado.setCliente(cliente);
        ventaContado.setSubtotal(1000L);
        ventaContado.setTotal(1000L);
        agregarDetalleDePrueba(ventaContado);

        ventaFinanciada = new VentaFinanciada();
        ventaFinanciada.setId(2L);
        ventaFinanciada.setCodigoComprobante("COMP-002");
        ventaFinanciada.setFecha(LocalDate.now());
        ventaFinanciada.setCliente(cliente);
        ventaFinanciada.setPorcentajeRecargo(10L);
        ventaFinanciada.setSubtotal(1000L);
        ventaFinanciada.setTotal(1100L);
        agregarDetalleDePrueba(ventaFinanciada);
    }

    private void agregarDetalleDePrueba(Venta venta) {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setCantidad(1);
        detalle.setPrecioBaseAplicado(1000L);
        detalle.setSubtotal(1000L);
        detalle.setProducto(producto);
        venta.agregarDetalle(detalle);
    }

    @Test
    void registrarVentaContadoExitosamente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(ventaRepository.save(any(VentaContado.class))).thenReturn(ventaContado);

        VentaResponseDTO resultado = ventaService.registrarVentaContado(requestContado);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(ModalidadPago.CONTADO, resultado.getModalidadPago());
        assertEquals(1, resultado.getDetalles().size());

        verify(ventaRepository).save(any(VentaContado.class));
    }

    @Test
    void registrarVentaFinanciadaExitosamente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(ventaRepository.save(any(VentaFinanciada.class))).thenReturn(ventaFinanciada);

        VentaResponseDTO resultado = ventaService.registrarVentaFinanciada(requestFinanciada);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals(ModalidadPago.FINANCIADA, resultado.getModalidadPago());
        assertEquals(1, resultado.getDetalles().size());

        verify(ventaRepository, atLeastOnce()).save(any(VentaFinanciada.class));
    }

    @Test
    void consultarVentasPorClienteExitosamente() {
        when(ventaRepository.findByClienteId(1L))
            .thenReturn(Arrays.asList(ventaContado));

        List<VentaResponseDTO> resultado = ventaService.consultarVentasPorCliente(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(ventaRepository).findByClienteId(1L);
    }

    @Test
    void consultarVentasPorRangoFechasExitosamente() {
        LocalDate hoy = LocalDate.now();

        when(ventaRepository.findByFechaBetween(
            hoy.minusDays(1),
            hoy.plusDays(1)
        )).thenReturn(Arrays.asList(ventaContado, ventaFinanciada));

        List<VentaResponseDTO> resultado = ventaService.consultarVentasPorRangoFechas(
            hoy.minusDays(1),
            hoy.plusDays(1)
        );

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(ventaRepository).findByFechaBetween(
            hoy.minusDays(1),
            hoy.plusDays(1)
        );
    }

    @Test
    void filtrarPorModalidadExitosamente() {
        when(ventaRepository.findAll())
            .thenReturn(Arrays.asList(ventaContado, ventaFinanciada));

        List<VentaResponseDTO> resultado = ventaService.filtrarPorModalidad("CONTADO");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(ModalidadPago.CONTADO, resultado.get(0).getModalidadPago());

        verify(ventaRepository).findAll();
    }
    @Test
    void registrarVentaContadoConDescuentoValidoAplicaDescuento() {
        VentaRequestDTO requestConDescuento = new VentaRequestDTO(
            1L,
            ModalidadPago.CONTADO,
            "COMP-010",
            List.of(new ItemVentaRequestDTO(1L, 2)),
            true,
            15L,
            0L
        );

        VentaContado guardadaConDescuento = new VentaContado();
        guardadaConDescuento.setId(10L);
        guardadaConDescuento.setCodigoComprobante("COMP-010");
        guardadaConDescuento.setFecha(LocalDate.now());
        guardadaConDescuento.setCliente(cliente);
        guardadaConDescuento.setCuponDescuentoValido(true);
        guardadaConDescuento.setSubtotal(2000L);
        guardadaConDescuento.setMontoDescuento(300L);
        guardadaConDescuento.setTotal(1700L);
        agregarDetalleDePrueba(guardadaConDescuento);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(ventaRepository.save(any(VentaContado.class))).thenReturn(guardadaConDescuento);

        VentaResponseDTO resultado = ventaService.registrarVentaContado(requestConDescuento);

        assertNotNull(resultado);
        assertEquals(2000L, resultado.getSubtotal());
        assertEquals(300L, resultado.getMontoDescuento());
        assertEquals(1700L, resultado.getTotal());
        assertEquals(8, producto.getStock());
    }

    @Test
    void registrarVentaContadoConPorcentajeInvalidoLanzaExcepcion() {
        VentaRequestDTO requestInvalido = new VentaRequestDTO(
            1L,
            ModalidadPago.CONTADO,
            "COMP-011",
            List.of(new ItemVentaRequestDTO(1L, 1)),
            true,
            120L,
            0L
        );

        assertThrows(SolicitudInvalidaException.class,
            () -> ventaService.registrarVentaContado(requestInvalido));

        verify(ventaRepository, never()).save(any());
    }

    @Test
    void registrarVentaSinStockSuficienteLanzaStockInsuficienteException() {
        producto.setStock(1);
        VentaRequestDTO requestSinStock = new VentaRequestDTO(
            1L,
            ModalidadPago.CONTADO,
            "COMP-012",
            List.of(new ItemVentaRequestDTO(1L, 3)),
            false,
            0L,
            0L
        );

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThrows(StockInsuficienteException.class,
            () -> ventaService.registrarVentaContado(requestSinStock));

        verify(ventaRepository, never()).save(any());
    }

    @Test
    void registrarVentaFinanciadaGeneraSeisCuotas() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        when(ventaRepository.save(any(VentaFinanciada.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        VentaResponseDTO resultado = ventaService.registrarVentaFinanciada(requestFinanciada);

        assertNotNull(resultado);
        assertEquals(1000L, resultado.getSubtotal());
        assertEquals(1100L, resultado.getTotal());
        assertEquals(6, resultado.getCuotas().size());
        verify(ventaRepository, times(2)).save(any(VentaFinanciada.class));
    }

    @Test
    void consultarVentasPorClienteConIdInvalidoLanzaExcepcion() {
        assertThrows(SolicitudInvalidaException.class,
            () -> ventaService.consultarVentasPorCliente(0L));

        verify(ventaRepository, never()).findByClienteId(any());
    }

    @Test
    void consultarVentasPorRangoFechasConInicioPosteriorAFinLanzaExcepcion() {
        LocalDate hoy = LocalDate.now();

        assertThrows(SolicitudInvalidaException.class,
            () -> ventaService.consultarVentasPorRangoFechas(hoy.plusDays(1), hoy));

        verify(ventaRepository, never()).findByFechaBetween(any(), any());
    }

    @Test
    void filtrarPorModalidadFinanciadaYModalidadInvalida() {
        when(ventaRepository.findAll())
            .thenReturn(Arrays.asList(ventaContado, ventaFinanciada));

        List<VentaResponseDTO> financiadas = ventaService.filtrarPorModalidad("FINANCIADA");

        assertEquals(1, financiadas.size());
        assertEquals(ModalidadPago.FINANCIADA, financiadas.get(0).getModalidadPago());

        assertThrows(SolicitudInvalidaException.class,
            () -> ventaService.filtrarPorModalidad("TARJETA"));
    }
    

	@Test
	void registrarVentaConClienteInexistenteLanzaExcepcion() {
	
	    when(clienteRepository.findById(99L))
	        .thenReturn(Optional.empty());
	
	    VentaRequestDTO request = new VentaRequestDTO(
	        99L,
	        ModalidadPago.CONTADO,
	        "COMP-020",
	        List.of(new ItemVentaRequestDTO(1L, 1)),
	        false,
	        0L,
	        0L
	    );
	
	    assertThrows(
	        RecursoNoEncontradoException.class,
	        () -> ventaService.registrarVentaContado(request)
	    );
	
	    verify(ventaRepository, never()).save(any());
	}
	
	@Test
	void registrarVentaConProductoInexistenteLanzaExcepcion() {
	
	    when(clienteRepository.findById(1L))
	        .thenReturn(Optional.of(cliente));
	
	    when(productoRepository.findById(99L))
	        .thenReturn(Optional.empty());
	
	    VentaRequestDTO request = new VentaRequestDTO(
	        1L,
	        ModalidadPago.CONTADO,
	        "COMP-021",
	        List.of(new ItemVentaRequestDTO(99L, 1)),
	        false,
	        0L,
	        0L
	    );
	
	    assertThrows(
	        RecursoNoEncontradoException.class,
	        () -> ventaService.registrarVentaContado(request)
	    );
	
	    verify(ventaRepository, never()).save(any());
	}
	

	@Test
	void registrarVentaConCantidadCeroLanzaExcepcion() {
	
	    VentaRequestDTO request = new VentaRequestDTO(
	        1L,
	        ModalidadPago.CONTADO,
	        "COMP-022",
	        List.of(new ItemVentaRequestDTO(1L, 0)),
	        false,
	        0L,
	        0L
	    );
	
	    assertThrows(
	        SolicitudInvalidaException.class,
	        () -> ventaService.registrarVentaContado(request)
	    );
	
	    verify(clienteRepository, never()).findById(any());
	    verify(productoRepository, never()).findById(any());
	    verify(ventaRepository, never()).save(any());
	}

	@Test
	void registrarVentaFinanciadaConRecargoNegativoLanzaExcepcion() {
	
	    VentaRequestDTO request = new VentaRequestDTO(
	        1L,
	        ModalidadPago.FINANCIADA,
	        "COMP-023",
	        List.of(new ItemVentaRequestDTO(1L, 1)),
	        false,
	        0L,
	        -10L
	    );
	
	    assertThrows(
	        SolicitudInvalidaException.class,
	        () -> ventaService.registrarVentaFinanciada(request)
	    );
	
	    verify(clienteRepository, never()).findById(any());
	    verify(ventaRepository, never()).save(any());
	}
	
	@Test
	void registrarVentaConSolicitudNulaLanzaExcepcion() {
	
	    assertThrows(
	        SolicitudInvalidaException.class,
	        () -> ventaService.registrarVentaContado(null)
	    );
	
	    verify(ventaRepository, never()).save(any());
	}
}