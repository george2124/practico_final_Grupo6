package ar.edu.unju.fi.poo.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	 @Column(nullable = false, length = 100)
	 private String nombre;

	 @Column(length = 255)
	 private String descripcion;

	 @Column(nullable = false, length = 50)
	 private String marca;

	 @Column(nullable = false)
	 private Integer stock;

	 @Column(nullable = false)
	 private long precioBase;

	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "categoria_id", nullable = false)
	 private Categoria categoria;

	 public Producto(Long id, String nombre, String descripcion, String marca, Integer stock, long precioBase,
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

	 public Long getId() {
		 return id;
	 }

	 public void setId(Long id) {
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

	 public long getPrecioBase() {
		 return precioBase;
	 }

	 public void setPrecioBase(long precioBase) {
		 this.precioBase = precioBase;
	 }

	 public Categoria getCategoria() {
		 return categoria;
	 }

	 public void setCategoria(Categoria categoria) {
		 this.categoria = categoria;
	 }
	
	
	 
	
}
