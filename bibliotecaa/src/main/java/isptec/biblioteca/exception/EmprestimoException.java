package isptec.biblioteca.exception;

/**
 * Exceção lançada quando um empréstimo não pode ser realizado.
 */
public class EmprestimoException extends BibliotecaException {

    public EmprestimoException(String message) {
        super(message);
    }

    public EmprestimoException(String message, Throwable cause) {
        super(message, cause);
    }

    // Métodos de fábrica para exceções comuns
    public static EmprestimoException limiteAtingido() {
        return new EmprestimoException("Limite de empréstimos atingido (máximo 3 livros).");
    }

    public static EmprestimoException membroBloqueado() {
        return new EmprestimoException("Membro bloqueado. Regularize sua situação.");
    }

    public static EmprestimoException livroIndisponivel() {
        return new EmprestimoException("Livro não disponível para empréstimo.");
    }

    public static EmprestimoException multaPendente() {
        return new EmprestimoException("Multa pendente acima do limite permitido.");
    }

    public static EmprestimoException renovacaoNaoPermitida() {
        return new EmprestimoException("Renovação não permitida. Limite atingido ou reserva pendente.");
    }
}

