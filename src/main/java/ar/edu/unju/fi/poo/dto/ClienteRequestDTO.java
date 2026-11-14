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
	
}
