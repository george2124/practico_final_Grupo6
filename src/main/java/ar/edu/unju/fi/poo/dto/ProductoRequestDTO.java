package ar.edu.unju.fi.poo.dto;



public class ProductoRequestDTO {
	 private String nombre;
	 private String descripcion;
	 private String marca;
	 private Integer stock;
	 private long precioBase;
	 private Long categoriaId; // Solo requerimos el ID de la categoría vinculada
	 
	 
	 public ProductoRequestDTO(String nombre, String descripcion, String marca, Integer stock, long precioBase,
			Long categoriaId) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.marca = marca;
		this.stock = stock;
		this.precioBase = precioBase;
		this.categoriaId = categoriaId;
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


	 public Long getCategoriaId() {
		 return categoriaId;
	 }


	 public void setCategoriaId(Long categoriaId) {
		 this.categoriaId = categoriaId;
	 }
	 
	 

}
