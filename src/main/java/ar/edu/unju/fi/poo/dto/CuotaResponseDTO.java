package ar.edu.unju.fi.poo.dto;

import java.time.LocalDate;

import ar.edu.unju.fi.poo.enums.EstadoCuota;

public class CuotaResponseDTO {
	private Long id;
	private Integer numeroCuota;
	private long importeAPagar;
	private LocalDate fechaVencimiento;
	private EstadoCuota estado;
	private long ventaId;
	private String codigoComprobante;

	public CuotaResponseDTO() {
	}

	public CuotaResponseDTO(Long id, Integer numeroCuota, long importeAPagar, LocalDate fechaVencimiento,
			EstadoCuota estado, long ventaId, String codigoComprobante) {
		this.id = id;
		this.numeroCuota = numeroCuota;
		this.importeAPagar = importeAPagar;
		this.fechaVencimiento = fechaVencimiento;
		this.estado = estado;
		this.ventaId = ventaId;
		this.codigoComprobante = codigoComprobante;
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

	public long getVentaId() {
		return ventaId;
	}

	public void setVentaId(long ventaId) {
		this.ventaId = ventaId;
	}

	public String getCodigoComprobante() {
		return codigoComprobante;
	}

	public void setCodigoComprobante(String codigoComprobante) {
		this.codigoComprobante = codigoComprobante;
	}
}