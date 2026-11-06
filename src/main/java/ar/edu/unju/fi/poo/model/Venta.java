package ar.edu.unju.fi.poo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "ventas")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Venta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "codigo_comprobante", nullable = false, unique = true, length = 50)
	private String codigoComprobante;
	@Column(nullable = false)
	private LocalDate fecha;
	@Column(nullable = false)
	private long subtotal; //se cambio por el BigDecimal
	@Column(nullable = false)
	private long total;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;
	
	 // Composición fuerte (CascadeType.ALL + orphanRemoval)
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    // Helper method para asegurar la bidireccionalidad de la composición
    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        detalle.setVenta(this);
    }
	
	public Venta(long id, String codigoComprobante, LocalDate fecha, long subtotal, long subTotal2, long total,
			Cliente cliente) {
		super();
		this.id = id;
		this.codigoComprobante = codigoComprobante;
		this.fecha = fecha;
		this.subtotal = subtotal;
		this.total = total;
		this.cliente = cliente;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodigoComprobante() {
		return codigoComprobante;
	}

	public void setCodigoComprobante(String codigoComprobante) {
		this.codigoComprobante = codigoComprobante;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public long getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(long subtotal) {
		this.subtotal = subtotal;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
