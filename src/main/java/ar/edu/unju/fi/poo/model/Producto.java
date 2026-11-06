package ar.edu.unju.fi.poo.model;

import java.math.BigDecimal;

public class Producto {
	private long id;
	private String nombre;
	private String descripcion;
	private String marca;
	private Integer stock;
	private BigDecimal precioBase;
	private Categoria categoria;
	
	
	public Producto(long id, String nombre, String descripcion, String marca, Integer stock, BigDecimal precioBase,
			Categoria categoria) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.marca = marca;
		this.stock = stock;
		this.precioBase = precioBase;
		this.categoria = categoria;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getMarca() {
		return marca;
	}


	public void setMarca(String marca) {
		this.marca = marca;
	}


	public Integer getStock() {
		return stock;
	}


	public void setStock(Integer stock) {
		this.stock = stock;
	}


	public BigDecimal getPrecioBase() {
		return precioBase;
	}


	public void setPrecioBase(BigDecimal precioBase) {
		this.precioBase = precioBase;
	}


	public Categoria getCategoria() {
		return categoria;
	}


	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	
	
}
