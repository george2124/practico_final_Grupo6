package ar.edu.unju.fi.poo.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumen del estado de cuenta de un cliente, incluyendo cuotas pendientes e historial de ventas")
public class EstadoCuentaResponseDTO {

	@Schema(description = "ID del cliente", example = "1")
	private Long clienteId;

	@Schema(description = "Nombre completo o razón social del cliente", example = "Lucía Gómez")
	private String clienteNombre;

	@Schema(description = "Cantidad de cuotas pendientes de pago", example = "3")
	private int cantidadCuotasPendientes;

	@Schema(description = "Monto total adeudado en cuotas pendientes", example = "45000")
	private long totalAdeudado;

	@Schema(description = "Listado de cuotas pendientes del cliente")
	private List<CuotaResponseDTO> cuotasPendientes;

	@Schema(description = "Historial de ventas asociadas al cliente")
	private List<VentaResponseDTO> historialVentas;

	public EstadoCuentaResponseDTO() {
	}

	public EstadoCuentaResponseDTO(
			Long clienteId,
			String clienteNombre,
			int cantidadCuotasPendientes,
			long totalAdeudado,
			List<CuotaResponseDTO> cuotasPendientes,
			List<VentaResponseDTO> historialVentas) {

		this.clienteId = clienteId;
		this.clienteNombre = clienteNombre;
		this.cantidadCuotasPendientes = cantidadCuotasPendientes;
		this.totalAdeudado = totalAdeudado;
		this.cuotasPendientes = cuotasPendientes;
		this.historialVentas = historialVentas;
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

	public int getCantidadCuotasPendientes() {
		return cantidadCuotasPendientes;
	}

	public void setCantidadCuotasPendientes(int cantidadCuotasPendientes) {
		this.cantidadCuotasPendientes = cantidadCuotasPendientes;
	}

	public long getTotalAdeudado() {
		return totalAdeudado;
	}

	public void setTotalAdeudado(long totalAdeudado) {
		this.totalAdeudado = totalAdeudado;
	}

	public List<CuotaResponseDTO> getCuotasPendientes() {
		return cuotasPendientes;
	}

	public void setCuotasPendientes(List<CuotaResponseDTO> cuotasPendientes) {
		this.cuotasPendientes = cuotasPendientes;
	}

	public List<VentaResponseDTO> getHistorialVentas() {
		return historialVentas;
	}

	public void setHistorialVentas(List<VentaResponseDTO> historialVentas) {
		this.historialVentas = historialVentas;
	}
}