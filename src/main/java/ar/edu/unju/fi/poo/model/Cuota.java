package ar.edu.unju.fi.poo.model;


import java.time.LocalDate;

import ar.edu.unju.fi.poo.enums.EstadoCuota;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuotas")
public class Cuota {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuota", nullable = false)
    private Integer numeroCuota; // Del 1 al 6

    @Column(nullable = false)
    private long importeAPagar;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuota estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_financiada_id", nullable = false)
    private VentaFinanciada ventaFinanciada;

	public Cuota(Long id, Integer numeroCuota, long importeAPagar, LocalDate fechaVencimiento, EstadoCuota estado,
			VentaFinanciada ventaFinanciada) {
		super();
		this.id = id;
		this.numeroCuota = numeroCuota;
		this.importeAPagar = importeAPagar;
		this.fechaVencimiento = fechaVencimiento;
		this.estado = estado;
		this.ventaFinanciada = ventaFinanciada;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public long getImporteAPagar() {
		return importeAPagar;
	}

	public void setImporteAPagar(long importeAPagar) {
		this.importeAPagar = importeAPagar;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public EstadoCuota getEstado() {
		return estado;
	}

	public void setEstado(EstadoCuota estado) {
		this.estado = estado;
	}

	public VentaFinanciada getVentaFinanciada() {
		return ventaFinanciada;
	}

	public void setVentaFinanciada(VentaFinanciada ventaFinanciada) {
		this.ventaFinanciada = ventaFinanciada;
	}
    
    
}
