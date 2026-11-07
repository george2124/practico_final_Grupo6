package ar.edu.unju.fi.poo.exception;

public class StockInsuficienteException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StockInsuficienteException(String message) {
        super(message);
    }
}