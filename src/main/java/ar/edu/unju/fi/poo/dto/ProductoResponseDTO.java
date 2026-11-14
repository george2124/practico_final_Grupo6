package ar.edu.unju.fi.poo.dto;

public class ProductoResponseDTO {
	private Long id;
    private String nombre;
    private String descripcion;
    private String marca;
    private Integer stock;
    private long precioBase;
    private Long categoriaId;
    private String categoriaNombre; // Añadimos el nombre para facilitar la lectura en el Front

    public ProductoResponseDTO() {}

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
