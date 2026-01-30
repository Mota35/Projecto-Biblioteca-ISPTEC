package isptec.biblioteca.domain.entities;

import isptec.biblioteca.domain.enumeracao.Perfil;

public class Bibliotecario extends Pessoa {
    private String funcionarioId;

    public Bibliotecario() {
        super();
        this.funcionarioId = "";
    }

    public Bibliotecario(int id, String nome, String email, String funcionarioId) {
        super(id, nome, email, Perfil.ADMIN);
        this.funcionarioId = funcionarioId;
    }

    public String getMatricula() {
        return funcionarioId;
    }

    public void setMatricula(String matricula) {
        this.funcionarioId = matricula;
    }
}
