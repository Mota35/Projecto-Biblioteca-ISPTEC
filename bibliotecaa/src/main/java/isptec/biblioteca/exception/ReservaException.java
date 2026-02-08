package isptec.biblioteca.exception;

/**
 * Exceção lançada quando uma reserva não pode ser realizada.
 */
public class ReservaException extends BibliotecaException {

    public ReservaException(String message) {
        super(message);
    }

    public ReservaException(String message, Throwable cause) {
        super(message, cause);
    }

    // Métodos de fábrica para exceções comuns
    public static ReservaException livroDisponivel() {
        return new ReservaException("Livro disponível. Faça o empréstimo diretamente.");
    }

    public static ReservaException reservaDuplicada() {
        return new ReservaException("Você já possui uma reserva ativa para este livro.");
    }

    public static ReservaException limiteAtingido() {
        return new ReservaException("Limite de reservas atingido.");
    }

    public static ReservaException reservaExpirada() {
        return new ReservaException("Reserva expirada.");
    }
}

