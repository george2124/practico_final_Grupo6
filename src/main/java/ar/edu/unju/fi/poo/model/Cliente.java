package ar.edu.unju.fi.poo.model;

import ar.edu.unju.fi.poo.enums.TipoCliente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name = "clientes", uniqueConstraints = {
    @UniqueConstraint(columnNames = "identificacion"),
    @UniqueConstraint(columnNames = "email")
})
public class Cliente {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	 @Column(nullable = false, length = 20)
	 private String identificacion; // DNI o CUIT

	 @Column(nullable = false, length = 100)
	 private String nombre;

	 @Column(name = "apellido_razon_social", nullable = false, length = 150)
	 private String apellidoRazonSocial;

	 @Column(length = 20)
	 private String telefono;

	 @Column(nullable = false, length = 100)
	 private String email;

	 @Column(length = 200)
	 private String direccion;

	 @Enumerated(EnumType.STRING)
	 @Column(name = "tipo_cliente", nullable = false)
	 private TipoCliente tipoCliente;

	 
	 public Cliente(Long id, String identificacion, String nombre, String apellidoRazonSocial, String telefono,
			String email, String direccion, TipoCliente tipoCliente) {
		super();
		this.id = id;
		this.identificacion = identificacion;
		this.nombre = nombre;
		this.apellidoRazonSocial = apellidoRazonSocial;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
		this.tipoCliente = tipoCliente;
	 }


	 public Long getId() {
		 return id;
	 }


	 public void setId(Long id) {
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
