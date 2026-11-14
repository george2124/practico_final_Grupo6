package ar.edu.unju.fi.poo.dto;

/** Un renglón de la compra: qué producto y cuántas unidades. */
public class ItemVentaRequestDTO {
	private Long productoId;
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