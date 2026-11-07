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

import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.model.Categoria;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        Categoria categoria =
            new Categoria(1L, "Electrodomesticos", null);

        producto = new Producto(
            1L,
            "Televisor",
            "Smart TV 32 pulgadas",
            "Samsung",
            10,
            100000L,
            categoria
        );
    }

    @Test
    void guardarProductoExitosamente() {
        when(productoRepository.save(producto))
            .thenReturn(producto);

        Producto resultado =
            productoService.guardarProducto(producto);

        assertNotNull(resultado);
        assertEquals("Televisor", resultado.getNombre());
        assertEquals(100000L, resultado.getPrecioBase());

        verify(productoRepository).save(producto);
    }

    @Test
    void buscarProductoPorIdExitosamente() {
        when(productoRepository.findById(1L))
            .thenReturn(Optional.of(producto));

        Producto resultado = productoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarProductoInexistenteLanzaExcepcion() {
        when(productoRepository.findById(99L))
            .thenReturn(Optional.empty());

        assertThrows(
            RecursoNoEncontradoException.class,
            () -> productoService.buscarPorId(99L)
        );
    }

    @Test
    void listarProductosConStockCritico() {
        when(productoRepository.findByStockLessThanEqual(5))
            .thenReturn(Arrays.asList(producto));

        List<Producto> resultado =
            productoService.listarStockCritico(5);

        assertEquals(1, resultado.size());
        verify(productoRepository)
            .findByStockLessThanEqual(5);
    }

    @Test
    void listarTodosLosProductos() {
        when(productoRepository.findAll())
            .thenReturn(Arrays.asList(producto));

        List<Producto> resultado =
            productoService.listarTodos();

        assertEquals(1, resultado.size());
        verify(productoRepository).findAll();
    }
}
