package ar.edu.unju.fi.poo.mapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ar.edu.unju.fi.poo.dto.CuotaResponseDTO;
import ar.edu.unju.fi.poo.dto.DetalleVentaResponseDTO;
import ar.edu.unju.fi.poo.dto.VentaRequestDTO;
import ar.edu.unju.fi.poo.dto.VentaResponseDTO;
import ar.edu.unju.fi.poo.enums.ModalidadPago;
import ar.edu.unju.fi.poo.model.Cliente;
import ar.edu.unju.fi.poo.model.Cuota;
import ar.edu.unju.fi.poo.model.DetalleVenta;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;


@Component
public final class VentaMapper {

	public VentaMapper() {
	}

	public static String nombreCompleto(Cliente c) {
		if (c == null) {
			return "";
		}
		String nombre = c.getNombre() != null ? c.getNombre() : "";
		String apellido = c.getApellidoRazonSocial() != null ? c.getApellidoRazonSocial() : "";
		return (nombre + " " + apellido).trim();
	}

	public static VentaContado toVentaContado(VentaRequestDTO request) {
		VentaContado venta = new VentaContado();
		completarDatosBase(venta, request);
		return venta;
	}

	public static VentaFinanciada toVentaFinanciada(VentaRequestDTO request) {
		VentaFinanciada venta = new VentaFinanciada();
		completarDatosBase(venta, request);
		return venta;
	}

	private static void completarDatosBase(Venta venta, VentaRequestDTO request) {
		if (request == null) {
			return;
		}
		if (request.getClienteId() != null) {
			Cliente cliente = new Cliente();
			cliente.setId(request.getClienteId());
			venta.setCliente(cliente);
		}
		venta.setFecha(LocalDate.now());
		venta.setCodigoComprobante(request.getCodigoComprobante());

		if (request.getItems() != null) {
			List<DetalleVenta> detalles = request.getItems().stream().map(item -> {
				DetalleVenta detalle = new DetalleVenta();
				if (item.getProductoId() != null) {
					Producto producto = new Producto();
					producto.setId(item.getProductoId());
					detalle.setProducto(producto);
				}
				detalle.setCantidad(item.getCantidad());
				detalle.setVenta(venta);
				return detalle;
			}).collect(Collectors.toList());
			venta.setDetalles(detalles);
		}
	}

	public static VentaResponseDTO toResponse(Venta venta) {
		if (venta == null) {
			return null;
		}

		VentaResponseDTO dto = new VentaResponseDTO();
		dto.setId(venta.getId());
		dto.setCodigoComprobante(venta.getCodigoComprobante());
		dto.setFecha(venta.getFecha());

		if (venta.getCliente() != null) {
			dto.setClienteId(venta.getCliente().getId());
			dto.setClienteNombre(nombreCompleto(venta.getCliente()));
		}

		dto.setSubtotal(venta.getSubtotal());
		dto.setTotal(venta.getTotal());

		if (venta.getDetalles() != null) {
			dto.setDetalles(venta.getDetalles().stream()
					.map(VentaMapper::toDetalleResponse)
					.collect(Collectors.toList()));
		} else {
			dto.setDetalles(Collections.emptyList());
		}

		if (venta instanceof VentaContado) {
			VentaContado vc = (VentaContado) venta;
			dto.setModalidadPago(ModalidadPago.CONTADO);
			dto.setCuponAplicado(vc.getCuponDescuentoValido());
			dto.setMontoDescuento(vc.getMontoDescuento());
		} else if (venta instanceof VentaFinanciada) {
			VentaFinanciada vf = (VentaFinanciada) venta;
			dto.setModalidadPago(ModalidadPago.FINANCIADA);
			dto.setPorcentajeRecargo(vf.getPorcentajeRecargo());
			if (vf.getCuotas() != null) {
				dto.setCuotas(vf.getCuotas().stream()
						.map(VentaMapper::toCuotaResponse)
						.collect(Collectors.toList()));
			} else {
				dto.setCuotas(Collections.emptyList());
			}
		}
		return dto;
	}

	public static List<VentaResponseDTO> toResponseList(List<? extends Venta> ventas) {
		if (ventas == null) {
			return Collections.emptyList();
		}
		return ventas.stream().map(VentaMapper::toResponse).collect(Collectors.toList());
	}

	public static DetalleVentaResponseDTO toDetalleResponse(DetalleVenta d) {
		if (d == null) {
			return null;
		}
		Long productoId = d.getProducto() != null ? d.getProducto().getId() : null;
		String productoNombre = d.getProducto() != null ? d.getProducto().getNombre() : null;
		return new DetalleVentaResponseDTO(
				productoId,
				productoNombre,
				d.getCantidad(),
				d.getPrecioBaseAplicado(),
				d.getSubtotal());
	}

	public static CuotaResponseDTO toCuotaResponse(Cuota c) {
		if (c == null) {
			return null;
		}
		Long ventaId = c.getVentaFinanciada() != null ? c.getVentaFinanciada().getId() : null;
		String codigoComprobante = c.getVentaFinanciada() != null ? c.getVentaFinanciada().getCodigoComprobante() : null;
		return new CuotaResponseDTO(
				c.getId(),
				c.getNumeroCuota(),
				c.getImporteAPagar(),
				c.getFechaVencimiento(),
				c.getEstado(),
				ventaId,
				codigoComprobante);
	}
}