package main.java.isptec.biblioteca.domain.entities;

import main.java.isptec.biblioteca.domain.enumeracao.Perfil;

public class Bibliotecario extends Pessoa {
    private String funcionarioId;

    public Bibliotecario(int id, String nome, String email, String funcionarioId) {
        super(id, nome, email, Perfil.ADMIN);
        this.funcionarioId = funcionarioId;
    }
}
