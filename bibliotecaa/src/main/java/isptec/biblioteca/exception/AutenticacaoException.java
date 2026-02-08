package isptec.biblioteca.exception;

/**
 * Exceção lançada quando a autenticação falha.
 */
public class AutenticacaoException extends BibliotecaException {

    public AutenticacaoException(String message) {
        super(message);
    }

    public AutenticacaoException(String message, Throwable cause) {
        super(message, cause);
    }

    // Métodos de fábrica para exceções comuns
    public static AutenticacaoException credenciaisInvalidas() {
        return new AutenticacaoException("Email ou senha incorretos.");
    }

    public static AutenticacaoException emailJaExiste() {
        return new AutenticacaoException("Email já está registado no sistema.");
    }

    public static AutenticacaoException matriculaJaExiste() {
        return new AutenticacaoException("Matrícula já está registada no sistema.");
    }

    public static AutenticacaoException sessaoExpirada() {
        return new AutenticacaoException("Sessão expirada. Faça login novamente.");
    }
}

