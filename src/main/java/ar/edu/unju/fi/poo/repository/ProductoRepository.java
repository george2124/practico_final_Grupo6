package ar.edu.unju.fi.poo.repository;

import java.math.BigDecimal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
 // Método personalizado para el filtrado avanzado con parámetros opcionales
    @Query("SELECT p FROM Producto p WHERE (:categoriaId IS NULL OR p.categoria.id = :categoriaId) " +
           "AND (:marca IS NULL OR p.marca = :marca) " +
           "AND (:precioMin IS NULL OR p.precioBase >= :precioMin) " +
           "AND (:precioMax IS NULL OR p.precioBase <= :precioMax)")
    List<Producto> filtrarCustom(@Param("categoriaId") Long categoriaId, 
                                @Param("marca") String marca, 
                                @Param("precioMin") Long precioMin, 
                                @Param("precioMax") Long precioMax);
}