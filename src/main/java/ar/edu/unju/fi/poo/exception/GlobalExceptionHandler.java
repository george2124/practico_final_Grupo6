package ar.edu.unju.fi.poo.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import ar.edu.unju.fi.poo.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Manejo centralizado de excepciones: mapea cada excepción de negocio al
 * código HTTP correspondiente y responde siempre con el mismo JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RecursoNoEncontradoException.class)
	public ResponseEntity<ErrorResponseDTO> handleNoEncontrado(RecursoNoEncontradoException ex, HttpServletRequest req) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage(), req);
	}

	@ExceptionHandler(StockInsuficienteException.class)
	public ResponseEntity<ErrorResponseDTO> handleStock(StockInsuficienteException ex, HttpServletRequest req) {
		return build(HttpStatus.CONFLICT, ex.getMessage(), req);
	}

	@ExceptionHandler(SolicitudInvalidaException.class)
	public ResponseEntity<ErrorResponseDTO> handleSolicitudInvalida(SolicitudInvalidaException ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponseDTO> handleJsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST,
				"El cuerpo de la solicitud no es un JSON válido o tiene valores incorrectos (enum/fecha/número)", req);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponseDTO> handleTipoParametro(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST,
				"Valor inválido para el parámetro '" + ex.getName() + "': " + ex.getValue(), req);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponseDTO> handleParametroFaltante(MissingServletRequestParameterException ex, HttpServletRequest req) {
		return build(HttpStatus.BAD_REQUEST, "Falta el parámetro obligatorio: " + ex.getParameterName(), req);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponseDTO> handleIntegridad(DataIntegrityViolationException ex, HttpServletRequest req) {
		return build(HttpStatus.CONFLICT,
				"La operación viola una restricción de datos (por ejemplo, un valor único ya existente)", req);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGenerico(Exception ex, HttpServletRequest req) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", req);
	}

	private ResponseEntity<ErrorResponseDTO> build(HttpStatus status, String mensaje, HttpServletRequest req) {
		ErrorResponseDTO body = new ErrorResponseDTO(LocalDateTime.now(), status.value(),
				status.getReasonPhrase(), mensaje, req.getRequestURI());
		return ResponseEntity.status(status).body(body);
	}
}