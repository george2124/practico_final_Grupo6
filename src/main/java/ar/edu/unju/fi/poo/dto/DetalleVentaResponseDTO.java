package ar.edu.unju.fi.poo.dto;

public class DetalleVentaResponseDTO {
	private Long productoId;
	private String productoNombre;
	private Integer cantidad;
	private long precioUnitario;
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