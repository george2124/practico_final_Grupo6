package ar.edu.unju.fi.poo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.unju.fi.poo.enums.TipoCliente;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.DetalleVenta;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.repository.VentaRepository;
import ar.edu.unju.fi.poo.service.impl.VentaServiceImpl;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private VentaContado ventaContado;
    private VentaFinanciada ventaFinanciada;
    private Cliente cliente;
    private Producto producto;

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

        // Producto de prueba
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Producto para pruebas");
        producto.setMarca("Marca");
        producto.setStock(10);
        producto.setPrecioBase(1000L);

        // Venta de contado
        ventaContado = new VentaContado();
        ventaContado.setId(1L);
        ventaContado.setFecha(LocalDate.now());
        ventaContado.setCliente(cliente);

        agregarDetalleDePrueba(ventaContado);

        // Venta financiada
        ventaFinanciada = new VentaFinanciada();
        ventaFinanciada.setId(2L);
        ventaFinanciada.setFecha(LocalDate.now());
        ventaFinanciada.setCliente(cliente);

        agregarDetalleDePrueba(ventaFinanciada);
    }

    // Agrega un producto a la venta para cumplir la validación
    private void agregarDetalleDePrueba(Venta venta) {

        DetalleVenta detalle = new DetalleVenta();

        detalle.setCantidad(1);
        detalle.setPrecioBaseAplicado(1000L);
        detalle.setSubtotal(1000L);
        detalle.setProducto(producto);

        // También establece la relación entre venta y detalle
        venta.agregarDetalle(detalle);
    }

    @Test
    void registrarVentaContadoExitosamente() {

        when(ventaRepository.save(ventaContado)).thenReturn(ventaContado);

        VentaContado resultado =
            ventaService.registrarVentaContado(ventaContado);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1, resultado.getDetalles().size());

        verify(ventaRepository).save(ventaContado);
    }

    @Test
    void registrarVentaFinanciadaExitosamente() {

        when(ventaRepository.save(ventaFinanciada)).thenReturn(ventaFinanciada);

        VentaFinanciada resultado =
            ventaService.registrarVentaFinanciada(ventaFinanciada);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals(1, resultado.getDetalles().size());

        verify(ventaRepository).save(ventaFinanciada);
    }

    @Test
    void consultarVentasPorClienteExitosamente() {

        when(ventaRepository.findByClienteId(1L))
            .thenReturn(Arrays.asList(ventaContado));

        List<?> resultado = ventaService.consultarVentasPorCliente(1L);

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

        List<?> resultado = ventaService.consultarVentasPorRangoFechas(
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

        when(ventaRepository.findByModalidadPago(VentaContado.class))
            .thenReturn(Arrays.asList(ventaContado));

        List<?> resultado = ventaService.filtrarPorModalidad("CONTADO");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(ventaRepository).findByModalidadPago(VentaContado.class);
    }
}
