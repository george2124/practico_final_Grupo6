package ar.edu.unju.fi.poo.dto;

import ar.edu.unju.fi.poo.enums.TipoCliente;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Datos devueltos al consultar, crear o actualizar un cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

	@Schema(description = "ID del cliente", example = "1")
	private long id;

	@Schema(description = "DNI o CUIT del cliente", example = "20384567891")
	private String identificador;

	@Schema(description = "Nombre del cliente", example = "Lucía")
	private String nombre;

	@Schema(description = "Apellido o razón social", example = "Gómez")
	private String apellidoRazonSocial;

	@Schema(description = "Teléfono de contacto", example = "3888451234")
	private String telefono;

	@Schema(description = "Correo electrónico", example = "lucia.gomez@ejemplo.com")
	private String email;

	@Schema(description = "Domicilio del cliente", example = "Av. Paterson 450, San Pedro")
	private String direccion;

	@Schema(description = "Tipo de cliente", example = "MINORISTA")
	private TipoCliente tipoCliente;

	//public ClienteResponseDTO(long id, String identificador, String nombre, String apellidoRazonSocial, String telefono,
		//	String email, String direccion, TipoCliente tipoCliente) {
		//super();
		//this.id = id;
		//this.identificador = identificador;
		//this.nombre = nombre;
		//this.apellidoRazonSocial = apellidoRazonSocial;
		//this.telefono = telefono;
		//this.email = email;
		//this.direccion = direccion;
		//this.tipoCliente = tipoCliente;
	//}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
