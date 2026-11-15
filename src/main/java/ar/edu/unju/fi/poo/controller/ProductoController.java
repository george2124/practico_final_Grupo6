package ar.edu.unju.fi.poo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unju.fi.poo.dto.ProductoRequestDTO;
import ar.edu.unju.fi.poo.dto.ProductoResponseDTO;
import ar.edu.unju.fi.poo.model.Categoria;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
	@Autowired
    private ProductoService productoService;

    // Guardar Producto (POST)
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@RequestBody ProductoRequestDTO request) {
        // Instanciamos un objeto Categoria simulado solo con el ID que viene del DTO
        Categoria categoriaAsignada = new Categoria();
        categoriaAsignada.setId(request.getCategoriaId());

        Producto nuevoProducto = new Producto(
            null, // El ID lo autoincrementa la BD
            request.getNombre(),
            request.getDescripcion(),
            request.getMarca(),
            request.getStock(),
            request.getPrecioBase(),
            categoriaAsignada
        );

        Producto productoGuardado = productoService.guardarProducto(nuevoProducto);
        return ResponseEntity.ok(convertirA_ResponseDTO(productoGuardado));
    }

    // Buscar por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarPorId(@PathVariable Long id) {
        Producto producto = productoService.buscarPorId(id).orElse(null);
        return ResponseEntity.ok(convertirA_ResponseDTO(producto));
    }

    // Listar todos los productos (GET)
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
        List<Producto> productos = productoService.listarTodos();
        List<ProductoResponseDTO> dtos = productos.stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Listar productos con stock crítico (GET)
    @GetMapping("/stock-critico")
    public ResponseEntity<List<ProductoResponseDTO>> listarStockCritico(@RequestParam Integer umbral) {
        List<Producto> productos = productoService.listarStockCritico(umbral);
        List<ProductoResponseDTO> dtos = productos.stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Filtrar productos avanzados (GET)
    @GetMapping("/filtrar")
    public ResponseEntity<List<ProductoResponseDTO>> filtrarProductos(
            @RequestParam Long categoriaId,
            @RequestParam String marca,
            @RequestParam Long precioMin,
            @RequestParam Long precioMax) {

        List<Producto> productos = productoService.filtrarProductos(categoriaId, marca, precioMin, precioMax);
        List<ProductoResponseDTO> dtos = productos.stream()
                .map(this::convertirA_ResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Método auxiliar de mapeo de Entidad a DTO de salida
    private ProductoResponseDTO convertirA_ResponseDTO(Producto producto) {
        // Prevenir NullPointerException si la categoría viene Lazy o vacía en las pruebas iniciales
        Long catId = (producto.getCategoria() != null) ? producto.getCategoria().getId() : null;
        String catNombre = (producto.getCategoria() != null) ? producto.getCategoria().getNombre() : "Sin Categoría";

        return new ProductoResponseDTO(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getMarca(),
            producto.getStock(),
            producto.getPrecioBase(),
            catId,
            catNombre
        );
    }
}
