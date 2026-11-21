package ar.edu.unju.fi.poo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos devueltos al consultar, crear o actualizar un producto")
public class ProductoResponseDTO {

	@Schema(description = "ID del producto", example = "1")
	private Long id;

	@Schema(description = "Nombre del producto", example = "Teclado Mecánico K587")
	private String nombre;

	@Schema(description = "Descripción del producto", example = "Switch blue, retroiluminado RGB")
	private String descripcion;

	@Schema(description = "Marca del producto", example = "Redragon")
	private String marca;

	@Schema(description = "Cantidad disponible en stock", example = "25")
	private Integer stock;

	@Schema(description = "Precio base unitario", example = "65000")
	private long precioBase;

	@Schema(description = "ID de la categoría asociada", example = "2")
	private Long categoriaId;

	@Schema(description = "Nombre de la categoría asociada", example = "Periféricos")
	private String categoriaNombre;

	public ProductoResponseDTO() {
	}

	public ProductoResponseDTO(Long id, String nombre, String descripcion, String marca, Integer stock, long precioBase,
			Long categoriaId, String categoriaNombre) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.marca = marca;
		this.stock = stock;
		this.precioBase = precioBase;
		this.categoriaId = categoriaId;
		this.categoriaNombre = categoriaNombre;
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

	public Long getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Long categoriaId) {
		this.categoriaId = categoriaId;
	}

	public String getCategoriaNombre() {
		return categoriaNombre;
	}

	public void setCategoriaNombre(String categoriaNombre) {
		this.categoriaNombre = categoriaNombre;
	}
}