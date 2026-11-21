package ar.edu.unju.fi.poo.dto;

import java.util.List;

import ar.edu.unju.fi.poo.enums.ModalidadPago;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Solicitud de registro de una venta.
 * - codigoComprobante es opcional: si no viene, el servicio lo genera.
 * - cuponValido / porcentajeDescuento solo se usan en CONTADO.
 * - porcentajeRecargo solo se usa en FINANCIADA.
 */
@Schema(description = "Datos requeridos para registrar una venta de contado o financiada")
public class VentaRequestDTO {

	@Schema(description = "ID del cliente que realiza la compra", example = "1")
	private Long clienteId;

	@Schema(description = "Modalidad de pago", example = "CONTADO")
	private ModalidadPago modalidadPago;

	@Schema(description = "Código de comprobante opcional (se autogenera si se omite)", example = "VTA-0001")
	private String codigoComprobante;

	@Schema(description = "Listado de productos y cantidades de la venta")
	private List<ItemVentaRequestDTO> items;

	@Schema(description = "Indica si aplica cupón de descuento (solo en CONTADO)", example = "true")
	private Boolean cuponValido;

	@Schema(description = "Porcentaje de descuento a aplicar (solo en CONTADO)", example = "10")
	private Long porcentajeDescuento;

	@Schema(description = "Porcentaje de recargo a aplicar (solo en FINANCIADA)", example = "15")
	private Long porcentajeRecargo;

	public VentaRequestDTO() {
	}

	public VentaRequestDTO(Long clienteId, ModalidadPago modalidadPago, String codigoComprobante,
			List<ItemVentaRequestDTO> items, Boolean cuponValido, Long porcentajeDescuento, Long porcentajeRecargo) {
		this.clienteId = clienteId;
		this.modalidadPago = modalidadPago;
		this.codigoComprobante = codigoComprobante;
		this.items = items;
		this.cuponValido = cuponValido;
		this.porcentajeDescuento = porcentajeDescuento;
		this.porcentajeRecargo = porcentajeRecargo;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public ModalidadPago getModalidadPago() {
		return modalidadPago;
	}

	public void setModalidadPago(ModalidadPago modalidadPago) {
		this.modalidadPago = modalidadPago;
	}

	public String getCodigoComprobante() {
		return codigoComprobante;
	}

	public void setCodigoComprobante(String codigoComprobante) {
		this.codigoComprobante = codigoComprobante;
	}

	public List<ItemVentaRequestDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemVentaRequestDTO> items) {
		this.items = items;
	}

	public Boolean getCuponValido() {
		return cuponValido;
	}

	public void setCuponValido(Boolean cuponValido) {
		this.cuponValido = cuponValido;
	}

	public Long getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public void setPorcentajeDescuento(Long porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	public Long getPorcentajeRecargo() {
		return porcentajeRecargo;
	}

	public void setPorcentajeRecargo(Long porcentajeRecargo) {
		this.porcentajeRecargo = porcentajeRecargo;
	}
}