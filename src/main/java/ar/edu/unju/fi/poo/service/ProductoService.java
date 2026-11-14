package ar.edu.unju.fi.poo.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import ar.edu.unju.fi.poo.model.Producto;

public interface ProductoService {
    
    Producto guardarProducto(Producto producto);
    
    Optional<Producto> buscarPorId(Long id);
    
    List<Producto> listarTodos();
    
    List<Producto> filtrarPorCategoria(Long categoriaId);
    
    List<Producto> filtrarPorMarca(String marca);
    
    List<Producto> filtrarPorRangoPrecio(BigDecimal min, BigDecimal max);
    
    List<Producto> consultarStockCritico(Integer umbral);
    
}