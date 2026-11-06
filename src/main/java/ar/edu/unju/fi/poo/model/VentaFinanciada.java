package ar.edu.unju.fi.poo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

public class VentaFinanciada {
	 
	@Column(nullable = false)
	private long porcentajeRecargo;

	// Una venta financiada genera un plan estricto de 6 cuotas consecutivas
	@OneToMany(mappedBy = "ventaFinanciada", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Cuota> cuotas = new ArrayList<>();

	public void agregarCuota(Cuota cuota) {
	     cuotas.add(cuota);
	     cuota.setVentaFinanciada(this);
	}

	public VentaFinanciada(long porcentajeRecargo, List<Cuota> cuotas) {
		super();
		this.porcentajeRecargo = porcentajeRecargo;
		this.cuotas = cuotas;
	}

	public long getPorcentajeRecargo() {
		return porcentajeRecargo;
	}

	public void setPorcentajeRecargo(long porcentajeRecargo) {
		this.porcentajeRecargo = porcentajeRecargo;
	}

	public List<Cuota> getCuotas() {
		return cuotas;
	}

	public void setCuotas(List<Cuota> cuotas) {
		this.cuotas = cuotas;
	}

		
	
}
