package ar.edu.unju.fi.poo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import ar.edu.unju.fi.poo.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@RequestBody ProductoRequestDTO request) {
        ProductoResponseDTO response = productoService.guardarProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/stock-critico")
    public ResponseEntity<List<ProductoResponseDTO>> listarStockCritico(@RequestParam Integer umbral) {
        return ResponseEntity.ok(productoService.listarStockCritico(umbral));
    }

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