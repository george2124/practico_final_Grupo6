package ar.edu.unju.fi.poo.exception;

/**
 * Se lanza cuando la solicitud llega bien formada pero con datos inválidos
 * (lista de productos vacía, cantidad <= 0, porcentajes fuera de rango, etc.).
 * El GlobalExceptionHandler la traduce a 400 Bad Request.
 */
public class SolicitudInvalidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SolicitudInvalidaException(String message) {
        super(message);
    }
}