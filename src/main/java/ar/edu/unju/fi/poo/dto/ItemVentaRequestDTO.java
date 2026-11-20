package ar.edu.unju.fi.poo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Un renglón de la compra: qué producto y cuántas unidades. */
@Schema(description = "Ítem de producto y cantidad dentro de una solicitud de venta")
public class ItemVentaRequestDTO {

	@Schema(description = "ID del producto a vender", example = "1")
	private Long productoId;

	@Schema(description = "Cantidad de unidades a comprar", example = "2")
	private Integer cantidad;

	public ItemVentaRequestDTO() {
	}

	public ItemVentaRequestDTO(Long productoId, Integer cantidad) {
		this.productoId = productoId;
		this.cantidad = cantidad;
	}

	public Long getProductoId() {
		return productoId;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}