package ar.edu.unju.fi.poo.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

/** Estructura JSON homogénea para TODOS los errores de la API. */
@Schema(description = "Estructura estándar de respuesta ante errores de la API")
public class ErrorResponseDTO {

	@Schema(description = "Fecha y hora en que ocurrió el error", example = "2026-11-20T19:20:00")
	private LocalDateTime timestamp;

	@Schema(description = "Código de estado HTTP", example = "404")
	private int status;

	@Schema(description = "Nombre del error HTTP", example = "Not Found")
	private String error;

	@Schema(description = "Detalle descriptivo del problema", example = "Cliente no encontrado con ID: 99")
	private String message;

	@Schema(description = "Ruta del endpoint que originó el error", example = "/api/clientes/99")
	private String path;

	public ErrorResponseDTO() {
	}

	public ErrorResponseDTO(LocalDateTime timestamp, int status, String error, String message, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}