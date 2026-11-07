package ar.edu.unju.fi.poo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "venta_financiada")
@PrimaryKeyJoinColumn(name = "venta_id")
public class VentaFinanciada extends Venta {

    @Column(name = "porcentaje_recargo", nullable = false)
    private long porcentajeRecargo;

    // Una venta financiada genera un plan estricto de 6 cuotas consecutivas
    @OneToMany(mappedBy = "ventaFinanciada", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cuota> cuotas = new ArrayList<>();

    public VentaFinanciada() {
        super();
    }

    public VentaFinanciada(long id, String codigoComprobante, LocalDate fecha, long subtotal, long total,
            Cliente cliente, long porcentajeRecargo, List<Cuota> cuotas) {
        super(id, codigoComprobante, fecha, subtotal, total, cliente);
        this.porcentajeRecargo = porcentajeRecargo;
        this.cuotas = cuotas;
    }

    public void agregarCuota(Cuota cuota) {
        cuotas.add(cuota);
        cuota.setVentaFinanciada(this);
    }

    public long getPorcentajeRecargo() {
        return porcentajeRecargo;
    }

    public void setPorcentajeRecargo(long porcentajeRecargo) {
        this.porcentajeRecargo = porcentajeRecargo;
    }

    public List<Cuota> getCuotas() {
        return cuotas;
    }

    public void setCuotas(List<Cuota> cuotas) {
        this.cuotas = cuotas;
    }
   
}