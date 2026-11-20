package ar.edu.unju.fi.poo.service;

import java.math.BigDecimal;
import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
import ar.edu.unju.fi.poo.model.Producto;
=======

import ar.edu.unju.fi.poo.dto.ProductoRequestDTO;
import ar.edu.unju.fi.poo.dto.ProductoResponseDTO;

public interface ProductoService {
>>>>>>> refs/remotes/origin/fix/desacoplamiento-dto

<<<<<<< HEAD
public interface ProductoService {
    
    Producto guardarProducto(Producto producto);
    
    Optional<Producto> buscarPorId(Long id);
    
    List<Producto> listarTodos();
    
    List<Producto> filtrarPorCategoria(Long categoriaId);
    
    List<Producto> filtrarPorMarca(String marca);
    
    List<Producto> filtrarPorRangoPrecio(BigDecimal min, BigDecimal max);
    
    List<Producto> consultarStockCritico(Integer umbral);
    
    List<Producto> listarStockCritico(Integer umbral);
    
    List<Producto> filtrarProductos(Long categoriaId, String marca, Long precioMin, Long precioMax);
    
=======
    ProductoResponseDTO guardarProducto(ProductoRequestDTO request);

    ProductoResponseDTO buscarPorId(Long id);

    List<ProductoResponseDTO> listarTodos();

    List<ProductoResponseDTO> listarStockCritico(Integer umbral);

    List<ProductoResponseDTO> filtrarProductos(Long categoriaId, String marca, Long precioMin, Long precioMax);
>>>>>>> refs/remotes/origin/fix/desacoplamiento-dto
}