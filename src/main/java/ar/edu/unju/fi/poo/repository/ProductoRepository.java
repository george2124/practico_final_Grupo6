package ar.edu.unju.fi.poo.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.poo.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Métodos individuales utilizados en el ServiceImpl
    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByMarcaIgnoreCase(String marca);

    List<Producto> findByPrecioBaseBetween(BigDecimal min, BigDecimal max);

    // Método combinado original por si lo usás en otro lado
    List<Producto> findByCategoriaIdAndMarcaAndPrecioBaseBetween(
        Long categoriaId,
        String marca,
        Long precioMin,
        Long precioMax
    );
    
    // Consultar productos con stock crítico o por debajo de un umbral
    List<Producto> findByStockLessThanEqual(Integer umbralStock);
    
}