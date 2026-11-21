package ar.edu.unju.fi.poo.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unju.fi.poo.dto.ItemVentaRequestDTO;
import ar.edu.unju.fi.poo.dto.ProductoRequestDTO;
import ar.edu.unju.fi.poo.dto.ProductoResponseDTO;
import ar.edu.unju.fi.poo.dto.VentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;
import ar.edu.unju.fi.poo.enums.ModalidadPago;
import ar.edu.unju.fi.poo.enums.TipoCliente;
import ar.edu.unju.fi.poo.model.Categoria;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.repository.ClienteRepository;
import ar.edu.unju.fi.poo.repository.ProductoRepository;
import ar.edu.unju.fi.poo.repository.VentaRepository;
import ar.edu.unju.fi.poo.service.ProductoService;
import ar.edu.unju.fi.poo.service.VentaService;

@SpringBootTest
@Transactional
class GestionComercialIntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    @PersistenceContext
    private EntityManager entityManager;

    private Categoria categoria;
    private Cliente cliente;

    @BeforeEach
    void prepararDatos() {

        // Crear una categoría para las pruebas
        categoria = new Categoria();
        categoria.setNombre("Periféricos");

        entityManager.persist(categoria);

        // Crear un cliente para las pruebas
        cliente = new Cliente(
                null,
                "20384567891",
                "Lucía",
                "Gómez",
                "3888451234",
                "lucia@mail.com",
                "San Pedro",
                TipoCliente.PARTICULAR
        );

        cliente = clienteRepository.save(cliente);

        entityManager.flush();
    }

    @Test
    void guardarYConsultarProductoEnBaseDeDatos() {

        // Crear producto utilizando el servicio real
        ProductoRequestDTO request = new ProductoRequestDTO(
                "Teclado Mecánico",
                "Teclado con iluminación RGB",
                "Redragon",
                20,
                65000L,
                categoria.getId()
        );

        ProductoResponseDTO productoGuardado =
                productoService.guardarProducto(request);

        assertNotNull(productoGuardado.getId());
        assertEquals("Teclado Mecánico", productoGuardado.getNombre());

        // Consultar el producto directamente en la base de datos
        Producto productoEncontrado = productoRepository
                .findById(productoGuardado.getId())
                .orElseThrow();

        assertEquals("Redragon", productoEncontrado.getMarca());
        assertEquals(20, productoEncontrado.getStock());
        assertEquals(65000L, productoEncontrado.getPrecioBase());

        // Comprobar la consulta de productos por stock crítico
        List<Producto> productosStockCritico =
                productoRepository.findByStockLessThanEqual(20);

        assertEquals(1, productosStockCritico.size());
    }

    @Test
    void registrarVentaContadoYActualizarStockEnBaseDeDatos() {

        // Crear un producto que pueda venderse
        ProductoRequestDTO productoRequest = new ProductoRequestDTO(
                "Mouse Gamer",
                "Mouse óptico",
                "Logitech",
                10,
                15000L,
                categoria.getId()
        );

        ProductoResponseDTO productoGuardado =
                productoService.guardarProducto(productoRequest);

        // Preparar la solicitud de venta de contado
        VentaRequestDTO ventaRequest = new VentaRequestDTO(
                cliente.getId(),
                ModalidadPago.CONTADO,
                "COMP-INTEGRACION-001",
                List.of(new ItemVentaRequestDTO(productoGuardado.getId(), 2)),
                false,
                0L,
                0L
        );

        // Registrar la venta utilizando el servicio real
        VentaResponseDTO ventaRegistrada =
                ventaService.registrarVentaContado(ventaRequest);

        assertNotNull(ventaRegistrada);
        assertEquals(30000L, ventaRegistrada.getTotal());
        assertEquals(ModalidadPago.CONTADO, ventaRegistrada.getModalidadPago());

        // Comprobar que la venta quedó guardada
        assertEquals(1, ventaRepository.findByClienteId(cliente.getId()).size());

        // Comprobar que el stock se actualizó en la base de datos
        Producto productoActualizado = productoRepository
                .findById(productoGuardado.getId())
                .orElseThrow();

        assertEquals(8, productoActualizado.getStock());
    }
}

