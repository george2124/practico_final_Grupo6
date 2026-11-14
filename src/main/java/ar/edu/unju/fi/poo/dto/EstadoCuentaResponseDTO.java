package ar.edu.unju.fi.poo.dto;

import java.util.List;

/** Estado de cuenta: cuotas pendientes de pago de un cliente y deuda total. */
public class EstadoCuentaResponseDTO {
	private Long clienteId;
	private String clienteNombre;
	private int cantidadCuotasPendientes;
	private long totalAdeudado;
	private List<CuotaResponseDTO> cuotasPendientes;

	public EstadoCuentaResponseDTO() {
	}

	public EstadoCuentaResponseDTO(Long clienteId, String clienteNombre, int cantidadCuotasPendientes,
			long totalAdeudado, List<CuotaResponseDTO> cuotasPendientes) {
		this.clienteId = clienteId;
		this.clienteNombre = clienteNombre;
		this.cantidadCuotasPendientes = cantidadCuotasPendientes;
		this.totalAdeudado = totalAdeudado;
		this.cuotasPendientes = cuotasPendientes;
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
}