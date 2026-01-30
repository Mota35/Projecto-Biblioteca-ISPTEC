package isptec.biblioteca.domain.entities;

import isptec.biblioteca.domain.enumeracao.Perfil;

public class Estudante extends Pessoa {
    private String matricula;

    public Estudante() {
        super();
        this.matricula = "";
    }

    public Estudante(int id, String nome, String email, String matricula) {
        super(id, nome, email, Perfil.USUARIO);
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
