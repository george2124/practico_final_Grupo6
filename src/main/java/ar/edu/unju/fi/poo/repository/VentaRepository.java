package ar.edu.unju.fi.poo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.poo.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta,Long> {
	
	// Historial de ventas asociadas a un cliente en particular
    List<Venta> findByClienteId(Long clienteId);

    // Ventas realizadas dentro de un período o rango de fechas específico
    List<Venta> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Filtrar ventas según la modalidad de pago utilizada (Contado o Financiada) usando JPQL
    @Query("SELECT v FROM Venta v WHERE TYPE(v) = :tipoVenta")
    List<Venta> findByModalidadPago(@Param("tipoVenta") Class<? extends Venta> tipoVenta);
}
