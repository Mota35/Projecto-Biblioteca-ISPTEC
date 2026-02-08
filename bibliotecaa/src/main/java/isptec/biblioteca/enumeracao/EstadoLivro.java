package isptec.biblioteca.enumeracao;

/**
 * Enumeração que representa o estado de um livro na biblioteca.
 */
public enum EstadoLivro {
    DISPONIVEL("Disponível"),
    EMPRESTADO("Emprestado"),
    RESERVADO("Reservado"),
    INDISPONIVEL("Indisponível");

    private final String descricao;

    EstadoLivro(String descricao) {
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

