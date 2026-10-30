package ar.edu.unju.fi.poo;

public class Cliente {
	private final int id;

	 // Representa a un cliente (hilo productor)
	public Cliente(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
}
