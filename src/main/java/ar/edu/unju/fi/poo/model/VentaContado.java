package ar.edu.unju.fi.poo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "venta_contado")
@PrimaryKeyJoinColumn(name = "venta_id")
public class VentaContado extends Venta {
	 
	@Column(name = "cupon_descuento_valido")
	 private Boolean cuponDescuentoValido;

	 @Column(nullable = false)
	 private long montoDescuento;

	 public VentaContado(long id, String codigoComprobante, LocalDate fecha, long subtotal, long subTotal2, long total,
			Cliente cliente, Boolean cuponDescuentoValido, long montoDescuento) {
		super(id, codigoComprobante, fecha, subtotal, subTotal2, total, cliente);
		this.cuponDescuentoValido = cuponDescuentoValido;
		this.montoDescuento = montoDescuento;
	 }

	 public Boolean getCuponDescuentoValido() {
		 return cuponDescuentoValido;
	 }

	 public void setCuponDescuentoValido(Boolean cuponDescuentoValido) {
		 this.cuponDescuentoValido = cuponDescuentoValido;
	 }

	 public long getMontoDescuento() {
		 return montoDescuento;
	 }

	 public void setMontoDescuento(long montoDescuento) {
		 this.montoDescuento = montoDescuento;
	 }
	 
	 
}
