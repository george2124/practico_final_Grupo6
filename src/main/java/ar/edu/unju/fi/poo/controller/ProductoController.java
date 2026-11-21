package ar.edu.unju.fi.poo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unju.fi.poo.dto.ProductoRequestDTO;
import ar.edu.unju.fi.poo.dto.ProductoResponseDTO;
import ar.edu.unju.fi.poo.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Productos", description = "Gestión, búsqueda, filtros y actualización de productos")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(
        summary = "Crear producto",
        description = "Registra un nuevo producto en el catálogo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@RequestBody ProductoRequestDTO request) {
        ProductoResponseDTO response = productoService.guardarProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Actualizar producto",
        description = "Modifica los datos de un producto existente por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, request));
    }

    @Operation(
        summary = "Buscar producto por ID",
        description = "Devuelve el detalle de un producto según su identificador."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @Operation(
        summary = "Listar todos los productos",
        description = "Obtiene todos los productos registrados."
    )
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @Operation(
        summary = "Listar productos con stock crítico",
        description = "Devuelve los productos cuyo stock es menor o igual al umbral indicado."
    )
    @GetMapping("/stock-critico")
    public ResponseEntity<List<ProductoResponseDTO>> listarStockCritico(@RequestParam Integer umbral) {
        return ResponseEntity.ok(productoService.listarStockCritico(umbral));
    }

    @Operation(
        summary = "Filtrar productos",
        description = "Filtra productos por categoría, marca y rango de precio base."
    )
    @GetMapping("/filtrar")
    public ResponseEntity<List<ProductoResponseDTO>> filtrarProductos(
            @RequestParam Long categoriaId,
            @RequestParam String marca,
            @RequestParam Long precioMin,
            @RequestParam Long precioMax) {
        return ResponseEntity.ok(
            productoService.filtrarProductos(categoriaId, marca, precioMin, precioMax)
        );
    }
}