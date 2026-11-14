package ar.edu.unju.fi.poo.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import ar.edu.unju.fi.poo.enums.ModalidadPago;

/**
 * Respuesta de una venta. Los campos propios de cada modalidad son nulos
 * (y no se serializan) cuando no corresponden:
 * - CONTADO: cuponAplicado, montoDescuento
 * - FINANCIADA: porcentajeRecargo, cuotas
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VentaResponseDTO {
	private long id;
	private String codigoComprobante;
	private LocalDate fecha;
	private ModalidadPago modalidadPago;
	private Long clienteId;
	private String clienteNombre;
	private long subtotal;
	private long total;
	private List<DetalleVentaResponseDTO> detalles;

	// Solo CONTADO
	private Boolean cuponAplicado;
	private Long montoDescuento;

	// Solo FINANCIADA
	private Long porcentajeRecargo;
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