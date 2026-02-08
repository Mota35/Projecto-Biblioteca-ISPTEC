package isptec.biblioteca.enumeracao;

/**
 * Enumeração que representa o perfil/papel do utilizador no sistema.
 */
public enum Perfil {
    ADMIN("Administrador"),
    USUARIO("Usuário"),
    BIBLIOTECARIO("Bibliotecário");

    private final String descricao;

    Perfil(String descricao) {
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

