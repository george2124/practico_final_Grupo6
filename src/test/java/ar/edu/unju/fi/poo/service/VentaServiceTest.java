package ar.edu.unju.fi.poo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.unju.fi.poo.enums.TipoCliente;
import ar.edu.unju.fi.poo.exception.StockInsuficienteException;
import ar.edu.unju.fi.poo.model.Categoria;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.repository.ProductoRepository;
import ar.edu.unju.fi.poo.repository.VentaRepository;

import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VentaService ventaService;

    private Cliente clienteTest;
    private Producto productoTest;

    @BeforeEach
    void setUp() {
        clienteTest = new Cliente(1L, "12345678", "Juan", "Perez", "3884123456", "juan@mail.com", "Calle Falsa 123", TipoCliente.PARTICULAR);
        
        Categoria categoria = new Categoria(1L, "Electro", null);
        productoTest = new Producto(1L, "Televisor", "Smart TV 32", "Samsung", 10, 1000L, categoria);
    }

    @Test
    void registrarVentaContadoExitosa() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTest));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoTest));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoTest);
        when(ventaRepository.save(any(VentaContado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var items = Collections.singletonList(new VentaService.ItemCompraDTO(1L, 2));

        VentaContado resultado = ventaService.registrarVentaContado("COMP-001", 1L, items, true, 10);

        assertNotNull(resultado);
        assertEquals("COMP-001", resultado.getCodigoComprobante());
        assertEquals(2000L, resultado.getSubtotal());
        assertEquals(1800L, resultado.getTotal()); // 2000 con 10% de descuento
        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(ventaRepository, times(1)).save(any(VentaContado.class));
    }

    @Test
    void registrarVentaStockInsuficienteLanzaExcepcion() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTest));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoTest));

        // Solicitamos 15 unidades cuando el stock es solo 10
        var items = Collections.singletonList(new VentaService.ItemCompraDTO(1L, 15));

        assertThrows(StockInsuficienteException.class, () -> {
            ventaService.registrarVentaContado("COMP-002", 1L, items, false, 0);
        });

        verify(ventaRepository, never()).save(any());
    }

    @Test
    void registrarVentaFinanciadaExitosaGeneraSeisCuotas() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteTest));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoTest));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoTest);
        
        when(ventaRepository.save(any(VentaFinanciada.class))).thenAnswer(invocation -> {
            VentaFinanciada v = invocation.getArgument(0);
            v.setId(10L);
            return v;
        });

        var items = Collections.singletonList(new VentaService.ItemCompraDTO(1L, 1)); // Precio base 1000

        VentaFinanciada resultado = ventaService.registrarVentaFinanciada("COMP-FIN-001", 1L, items, 20); // 20% recargo

        assertNotNull(resultado);
        assertEquals(1000L, resultado.getSubtotal());
        assertEquals(1200L, resultado.getTotal()); // 1000 + 20% de recargo
        assertEquals(6, resultado.getCuotas().size()); // Valida que se crearon exactamente las 6 cuotas requeridas
        assertEquals(200L, resultado.getCuotas().get(0).getImporteAPagar()); // 1200 / 6 = 200 por cuota
    }
    

@Test
void registrarVentaContadoSinCupon() {
    when(clienteRepository.findById(1L))
        .thenReturn(Optional.of(clienteTest));

    when(productoRepository.findById(1L))
        .thenReturn(Optional.of(productoTest));

    when(productoRepository.save(any(Producto.class)))
        .thenReturn(productoTest);

    when(ventaRepository.save(any(VentaContado.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    var items = Collections.singletonList(
        new VentaService.ItemCompraDTO(1L, 2)
    );

    VentaContado resultado = ventaService.registrarVentaContado(
        "COMP-003", 1L, items, false, 10
    );

    assertEquals(2000L, resultado.getSubtotal());
    assertEquals(0L, resultado.getMontoDescuento());
    assertEquals(2000L, resultado.getTotal());
}

@Test
void registrarVentaConClienteInexistente() {
    when(clienteRepository.findById(99L))
        .thenReturn(Optional.empty());

    var items = Collections.singletonList(
        new VentaService.ItemCompraDTO(1L, 1)
    );

    assertThrows(
        RecursoNoEncontradoException.class,
        () -> ventaService.registrarVentaContado(
            "COMP-004", 99L, items, false, 0
        )
    );

    verify(ventaRepository, never()).save(any());
}

@Test
void registrarVentaConProductoInexistente() {
    when(clienteRepository.findById(1L))
        .thenReturn(Optional.of(clienteTest));

    when(productoRepository.findById(99L))
        .thenReturn(Optional.empty());

    var items = Collections.singletonList(
        new VentaService.ItemCompraDTO(99L, 1)
    );

    assertThrows(
        RecursoNoEncontradoException.class,
        () -> ventaService.registrarVentaContado(
            "COMP-005", 1L, items, false, 0
        )
    );

    verify(ventaRepository, never()).save(any());
}

@Test
void registrarVentaFinanciadaConStockInsuficiente() {
    when(clienteRepository.findById(1L))
        .thenReturn(Optional.of(clienteTest));

    when(productoRepository.findById(1L))
        .thenReturn(Optional.of(productoTest));

    var items = Collections.singletonList(
        new VentaService.ItemCompraDTO(1L, 20)
    );

    assertThrows(
        StockInsuficienteException.class,
        () -> ventaService.registrarVentaFinanciada(
            "COMP-006", 1L, items, 20
        )
    );

    verify(ventaRepository, never()).save(any());
}

@Test
void ventaContadoActualizaElStock() {
    when(clienteRepository.findById(1L))
        .thenReturn(Optional.of(clienteTest));

    when(productoRepository.findById(1L))
        .thenReturn(Optional.of(productoTest));

    when(productoRepository.save(any(Producto.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    when(ventaRepository.save(any(VentaContado.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    var items = Collections.singletonList(
        new VentaService.ItemCompraDTO(1L, 3)
    );

    ventaService.registrarVentaContado(
        "COMP-007", 1L, items, false, 0
    );

    assertEquals(7, productoTest.getStock());
}

@Test
void ventaFinanciadaGeneraCuotasAdeudadas() {
    when(clienteRepository.findById(1L))
        .thenReturn(Optional.of(clienteTest));

    when(productoRepository.findById(1L))
        .thenReturn(Optional.of(productoTest));

    when(productoRepository.save(any(Producto.class)))
        .thenReturn(productoTest);

    when(ventaRepository.save(any(VentaFinanciada.class)))
        .thenAnswer(invocation -> {
            VentaFinanciada venta = invocation.getArgument(0);
            venta.setId(10L);
            return venta;
        });

    var items = Collections.singletonList(
        new VentaService.ItemCompraDTO(1L, 1)
    );

    VentaFinanciada resultado =
        ventaService.registrarVentaFinanciada(
            "COMP-008", 1L, items, 20
        );

    assertEquals(6, resultado.getCuotas().size());

    resultado.getCuotas().forEach(cuota ->
        assertEquals(
            ar.edu.unju.fi.poo.enums.EstadoCuota.ADEUDADA,
            cuota.getEstado()
    )
   );
 } 
}