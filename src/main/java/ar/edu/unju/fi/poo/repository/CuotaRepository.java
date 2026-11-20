package ar.edu.unju.fi.poo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.poo.enums.EstadoCuota;
import ar.edu.unju.fi.poo.model.Cuota;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    // Consultar el estado de cuenta de las cuotas pendientes de un cliente en particular
    @Query("SELECT c FROM Cuota c JOIN c.ventaFinanciada v WHERE v.cliente.id = :clienteId AND c.estado = :estado")
    List<Cuota> findByClienteIdAndEstado(@Param("clienteId") Long clienteId, @Param("estado") EstadoCuota estado);
    
    // se agrega, Buscar la primera cuota NO pagada de una venta, ordenada de la 1 a la 6
    Optional<Cuota> findFirstByVentaFinanciadaIdAndEstadoNotOrderByNumeroCuotaAsc(Long ventaId, EstadoCuota estado);

    // se agrega y Verifica si todavía quedan cuotas sin pagar en esa venta
    boolean existsByVentaFinanciadaIdAndEstadoNot(Long ventaId, EstadoCuota estado);
}