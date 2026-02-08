package isptec.biblioteca.model.entities;

import isptec.biblioteca.enumeracao.Perfil;
import java.util.Objects;

/**
 * Classe abstrata base para todas as pessoas no sistema.
 * Implementa herança para Membro e Bibliotecario.
 */
public abstract class Pessoa {
    protected int id;
    protected String nome;
    protected String email;
    protected String senha;
    protected Perfil perfil;

    protected Pessoa() {
        this.id = 0;
        this.nome = "";
        this.email = "";
        this.senha = "";
        this.perfil = null;
    }

    protected Pessoa(int id, String nome, String email, Perfil perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.senha = "";
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    /**
     * Método abstrato para obter identificador único (matrícula ou funcionarioId)
     */
    public abstract String getIdentificador();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return id == pessoa.id && Objects.equals(email, pessoa.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Pessoa{id=" + id + ", nome='" + nome + "', email='" + email + "', perfil=" + perfil + "}";
    }
}
