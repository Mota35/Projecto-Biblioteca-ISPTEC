package isptec.biblioteca.enumeracao;

/**
 * Enumeração que representa o estado de um empréstimo.
 */
public enum EstadoEmprestimo {
    ATIVO("Ativo"),
    DEVOLVIDO("Devolvido"),
    ATRASADO("Atrasado");

    private final String descricao;

    EstadoEmprestimo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
