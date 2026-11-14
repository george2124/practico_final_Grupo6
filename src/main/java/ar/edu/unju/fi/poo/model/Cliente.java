package ar.edu.unju.fi.poo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellidoRazonSocial;
    private String tipoDocumento;
    private String identificacion;
    private String telefono;
    private String email;
    private String ciudad;
    private String tipoCliente; // O podes ajustarlo según tu modelo

    // Constructor vacío (necesario para JPA y frameworks)
    public Cliente() {
    }

    // Constructor con todos los parámetros
    public Cliente(Long id, String nombre, String apellidoRazonSocial, String tipoDocumento, 
                   String identificacion, String telefono, String email, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoRazonSocial = apellidoRazonSocial;
        this.tipoDocumento = tipoDocumento;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.email = email;
        this.ciudad = ciudad;
    }

    // Getters y Setters
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

    public String getApellidoRazonSocial() {
        return apellidoRazonSocial;
    }

    public void setApellidoRazonSocial(String apellidoRazonSocial) {
        this.apellidoRazonSocial = apellidoRazonSocial;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
}