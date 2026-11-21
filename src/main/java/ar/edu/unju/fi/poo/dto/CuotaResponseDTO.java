package ar.edu.unju.fi.poo.dto;

import java.time.LocalDate;

import ar.edu.unju.fi.poo.enums.EstadoCuota;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información de una cuota correspondiente a una venta financiada")
public class CuotaResponseDTO {

	@Schema(description = "ID de la cuota", example = "1")
	private Long id;

	@Schema(description = "Número correlativo de cuota (1 a 6)", example = "1")
	private Integer numeroCuota;

	@Schema(description = "Importe a pagar de la cuota", example = "25000")
	private long importeAPagar;

	@Schema(description = "Fecha de vencimiento de la cuota", example = "2026-12-20")
	private LocalDate fechaVencimiento;

	@Schema(description = "Estado actual de la cuota", example = "ADEUDADA")
	private EstadoCuota estado;

	@Schema(description = "ID de la venta financiada asociada", example = "10")
	private long ventaId;

	@Schema(description = "Código de comprobante de la venta asociada", example = "COMP-A1B2C3D4")
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