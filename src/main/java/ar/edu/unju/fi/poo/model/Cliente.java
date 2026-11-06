package ar.edu.unju.fi.poo.model;

import ar.edu.unju.fi.poo.enums.TipoCliente;

public class Cliente {
	private long id;
	private String identificacion;
	private String nombre;
	private String apellidoRazonSocial;
	private String telefono;
	private String mail;
	private String direccion;
	private TipoCliente tipoCliente;
	
	
	public Cliente(long id, String identificacion, String nombre, String apellidoRazonSocial, String telefono,
			String mail, String direccion, TipoCliente tipoCliente) {
		super();
		this.id = id;
		this.identificacion = identificacion;
		this.nombre = nombre;
		this.apellidoRazonSocial = apellidoRazonSocial;
		this.telefono = telefono;
		this.mail = mail;
		this.direccion = direccion;
		this.tipoCliente = tipoCliente;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getIdentificacion() {
		return identificacion;
	}


	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getApellidoRazonSocial() {
		return apellidoRazonSocial;
	}


	public void setApellidoRazonSocial(String apellidoRazonSocial) {
		this.apellidoRazonSocial = apellidoRazonSocial;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}


	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	
	
	
}
