package isptec.biblioteca.domain.entities;

import isptec.biblioteca.domain.enumeracao.Perfil;

public abstract class Pessoa {
    protected int id;
    protected String nome;
    protected String email;
    protected Perfil perfil;

    public Pessoa(int id, String nome, String email, Perfil perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
    }

}
