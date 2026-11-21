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
}