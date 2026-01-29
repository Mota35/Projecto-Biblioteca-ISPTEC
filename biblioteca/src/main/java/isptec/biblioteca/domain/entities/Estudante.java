package main.java.isptec.biblioteca.domain.entities;

import main.java.isptec.biblioteca.domain.enumeracao.Perfil;

public class Estudante extends Pessoa {
    private String matricula;

    public Estudante(int id, String nome, String email, String matricula) {
        super(id, nome, email, Perfil.USUARIO);
        this.matricula = matricula;
    }
}
