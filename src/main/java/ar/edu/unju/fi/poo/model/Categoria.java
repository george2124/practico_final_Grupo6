package ar.edu.unju.fi.poo.model;

public class Categoria {
	private long id;
	private String nombre;
	
	
	public Categoria(long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
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
	
	
}
