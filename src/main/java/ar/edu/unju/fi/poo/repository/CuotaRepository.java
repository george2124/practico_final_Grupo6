package ar.edu.unju.fi.poo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.poo.model.Cuota;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    // Recupera todas las cuotas de un cliente,
    // ordenadas por fecha de venta y número de cuota.
    @Query("""
        SELECT c
        FROM Cuota c
        JOIN FETCH c.ventaFinanciada v
        JOIN FETCH v.cliente cl
        WHERE cl.id = :clienteId
        ORDER BY v.fecha ASC, c.numeroCuota ASC
    """)
    List<Cuota> buscarCuotasPorCliente(
            @Param("clienteId") Long clienteId);

    // Recupera todas las cuotas de una venta financiada
    // en orden correlativo.
    @Query("""
        SELECT c
        FROM Cuota c
        JOIN FETCH c.ventaFinanciada v
        WHERE v.id = :ventaId
        ORDER BY c.numeroCuota ASC
    """)
    List<Cuota> buscarCuotasPorVenta(
            @Param("ventaId") Long ventaId);
}
