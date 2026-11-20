package ar.edu.unju.fi.poo.service;

import java.util.List;

import ar.edu.unju.fi.poo.dto.ProductoRequestDTO;
import ar.edu.unju.fi.poo.dto.ProductoResponseDTO;

public interface ProductoService {

    ProductoResponseDTO guardarProducto(ProductoRequestDTO request);

    ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO request);

    ProductoResponseDTO buscarPorId(Long id);

    List<ProductoResponseDTO> listarTodos();

    List<ProductoResponseDTO> listarStockCritico(Integer umbral);

    List<ProductoResponseDTO> filtrarProductos(Long categoriaId, String marca, Long precioMin, Long precioMax);
}