package ar.edu.unju.fi.poo.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import ar.edu.unju.fi.poo.enums.ModalidadPago;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Respuesta de una venta. Los campos propios de cada modalidad son nulos
 * (y no se serializan) cuando no corresponden:
 * - CONTADO: cuponAplicado, montoDescuento
 * - FINANCIADA: porcentajeRecargo, cuotas
 */
@Schema(description = "Datos devueltos al consultar o registrar una venta")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VentaResponseDTO {

	@Schema(description = "ID de la venta", example = "1")
	private long id;

	@Schema(description = "Código de comprobante de la venta", example = "COMP-A1B2C3D4")
	private String codigoComprobante;

	@Schema(description = "Fecha de emisión de la venta", example = "2026-11-20")
	private LocalDate fecha;

	@Schema(description = "Modalidad de pago de la venta", example = "CONTADO")
	private ModalidadPago modalidadPago;

	@Schema(description = "ID del cliente asociado", example = "1")
	private Long clienteId;

	@Schema(description = "Nombre completo o razón social del cliente", example = "Lucía Gómez")
	private String clienteNombre;

	@Schema(description = "Subtotal antes de descuentos o recargos", example = "50000")
	private long subtotal;

	@Schema(description = "Total final de la venta", example = "45000")
	private long total;

	@Schema(description = "Detalle de ítems vendidos")
	private List<DetalleVentaResponseDTO> detalles;

	// Solo CONTADO
	@Schema(description = "Indica si se aplicó cupón válido (solo en CONTADO)", example = "true")
	private Boolean cuponAplicado;

	@Schema(description = "Monto descontado en pesos (solo en CONTADO)", example = "5000")
	private Long montoDescuento;

	// Solo FINANCIADA
	@Schema(description = "Porcentaje de recargo aplicado (solo en FINANCIADA)", example = "15")
	private Long porcentajeRecargo;

	@Schema(description = "Plan de cuotas generado (solo en FINANCIADA)")
	private List<CuotaResponseDTO> cuotas;

	public VentaResponseDTO() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodigoComprobante() {
		return codigoComprobante;
	}

	public void setCodigoComprobante(String codigoComprobante) {
		this.codigoComprobante = codigoComprobante;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public ModalidadPago getModalidadPago() {
		return modalidadPago;
	}

	public void setModalidadPago(ModalidadPago modalidadPago) {
		this.modalidadPago = modalidadPago;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public String getClienteNombre() {
		return clienteNombre;
	}

	public void setClienteNombre(String clienteNombre) {
		this.clienteNombre = clienteNombre;
	}

	public long getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(long subtotal) {
		this.subtotal = subtotal;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<DetalleVentaResponseDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleVentaResponseDTO> detalles) {
		this.detalles = detalles;
	}

	public Boolean getCuponAplicado() {
		return cuponAplicado;
	}

	public void setCuponAplicado(Boolean cuponAplicado) {
		this.cuponAplicado = cuponAplicado;
	}

	public Long getMontoDescuento() {
		return montoDescuento;
	}

	public void setMontoDescuento(Long montoDescuento) {
		this.montoDescuento = montoDescuento;
	}

	public Long getPorcentajeRecargo() {
		return porcentajeRecargo;
	}

	public void setPorcentajeRecargo(Long porcentajeRecargo) {
		this.porcentajeRecargo = porcentajeRecargo;
	}

	public List<CuotaResponseDTO> getCuotas() {
		return cuotas;
	}

	public void setCuotas(List<CuotaResponseDTO> cuotas) {
		this.cuotas = cuotas;
	}
}