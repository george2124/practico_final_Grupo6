package ar.edu.unju.fi.poo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.unju.fi.poo.dto.CuotaResponseDTO;
import ar.edu.unju.fi.poo.dto.DetalleVentaResponseDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;
import ar.edu.unju.fi.poo.enums.ModalidadPago;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.Cuota;
import ar.edu.unju.fi.poo.model.DetalleVenta;
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;

/**
 * Convierte entidades de venta a DTOs.
 * IMPORTANTE: se usa SIEMPRE dentro de un método @Transactional del servicio,
 * porque cliente, detalles y productos son LAZY.
 */
public final class VentaMapper {

	private VentaMapper() {
	}

	public static String nombreCompleto(Cliente c) {
		return c.getNombre() + " " + c.getApellidoRazonSocial();
	}

	public static VentaResponseDTO toResponse(Venta venta) {
		VentaResponseDTO dto = new VentaResponseDTO();
		dto.setId(venta.getId());
		dto.setCodigoComprobante(venta.getCodigoComprobante());
		dto.setFecha(venta.getFecha());
		dto.setClienteId(venta.getCliente().getId());
		dto.setClienteNombre(nombreCompleto(venta.getCliente()));
		dto.setSubtotal(venta.getSubtotal());
		dto.setTotal(venta.getTotal());
		dto.setDetalles(venta.getDetalles().stream()
				.map(VentaMapper::toDetalleResponse)
				.collect(Collectors.toList()));

		if (venta instanceof VentaContado) {
			VentaContado vc = (VentaContado) venta;
			dto.setModalidadPago(ModalidadPago.CONTADO);
			dto.setCuponAplicado(vc.getCuponDescuentoValido());
			dto.setMontoDescuento(vc.getMontoDescuento());
		} else if (venta instanceof VentaFinanciada) {
			VentaFinanciada vf = (VentaFinanciada) venta;
			dto.setModalidadPago(ModalidadPago.FINANCIADA);
			dto.setPorcentajeRecargo(vf.getPorcentajeRecargo());
			dto.setCuotas(vf.getCuotas().stream()
					.map(VentaMapper::toCuotaResponse)
					.collect(Collectors.toList()));
		}
		return dto;
	}

	public static List<VentaResponseDTO> toResponseList(List<? extends Venta> ventas) {
		return ventas.stream().map(VentaMapper::toResponse).collect(Collectors.toList());
	}

	public static DetalleVentaResponseDTO toDetalleResponse(DetalleVenta d) {
		return new DetalleVentaResponseDTO(
				d.getProducto().getId(),
				d.getProducto().getNombre(),
				d.getCantidad(),
				d.getPrecioBaseAplicado(),
				d.getSubtotal());
	}

	public static CuotaResponseDTO toCuotaResponse(Cuota c) {
		return new CuotaResponseDTO(
				c.getId(),
				c.getNumeroCuota(),
				c.getImporteAPagar(),
				c.getFechaVencimiento(),
				c.getEstado(),
				c.getVentaFinanciada().getId(),
				c.getVentaFinanciada().getCodigoComprobante());
	}
}
