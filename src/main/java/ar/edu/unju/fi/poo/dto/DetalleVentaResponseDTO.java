package ar.edu.unju.fi.poo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalle de un producto incluido dentro de una venta")
public class DetalleVentaResponseDTO {

	@Schema(description = "ID del producto vendido", example = "1")
	private Long productoId;

	@Schema(description = "Nombre del producto vendido", example = "Teclado Mecánico K587")
	private String productoNombre;

	@Schema(description = "Cantidad de unidades vendidas", example = "2")
	private Integer cantidad;

	@Schema(description = "Precio unitario aplicado al momento de la venta", example = "65000")
	private long precioUnitario;

	@Schema(description = "Subtotal del renglón (cantidad por precio unitario)", example = "130000")
	private long subtotal;

	public DetalleVentaResponseDTO() {
	}

	public DetalleVentaResponseDTO(Long productoId, String productoNombre, Integer cantidad, long precioUnitario,
			long subtotal) {
		this.productoId = productoId;
		this.productoNombre = productoNombre;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.subtotal = subtotal;
	}

	public Long getProductoId() {
		return productoId;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}

	public String getProductoNombre() {
		return productoNombre;
	}

	public void setProductoNombre(String productoNombre) {
		this.productoNombre = productoNombre;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public long getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(long precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public long getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(long subtotal) {
		this.subtotal = subtotal;
	}
}