package ar.edu.unju.fi.poo.dto;

import ar.edu.unju.fi.poo.enums.TipoCliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {
	
	private String identificador; //DNI / CUIT
	private String nombre;
	private String apellidoRazonSocial;
	private String telefono;
	private String email;
	private String direccion;
	private TipoCliente tipoCliente;
	
	
	public ClienteRequestDTO(String identificador, String nombre, String apellidoRazonSocial, String telefono,
			String email, String direccion, TipoCliente tipoCliente) {
		super();
		this.identificador = identificador;
		this.nombre = nombre;
		this.apellidoRazonSocial = apellidoRazonSocial;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
		this.tipoCliente = tipoCliente;
	}


	public String getIdentificador() {
		return identificador;
	}


	public void setIdentificador(String identificador) {
		this.identificador = identificador;
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
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
