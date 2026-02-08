package isptec.biblioteca.exception;

/**
 * Exceção base para erros de negócio da biblioteca.
 */
public class BibliotecaException extends RuntimeException {

    public BibliotecaException(String message) {
        super(message);
    }

    public BibliotecaException(String message, Throwable cause) {
        super(message, cause);
    }
}
