package isptec.biblioteca.model.entities;

import java.util.Objects;

/**
 * Classe Categoria - Representa uma categoria de livros.
 */
public class Categoria {

    private int id;
    private String nome;
    private String descricao;

    public Categoria() {
        this.id = 0;
        this.nome = "";
        this.descricao = "";
    }

    public Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.descricao = "";
    }

    public Categoria(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return id == categoria.id || Objects.equals(nome, categoria.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "Categoria{id=" + id + ", nome='" + nome + "'" +
               (descricao != null && !descricao.isEmpty() ? ", descricao='" + descricao + "'" : "") +
               "}";
    }
}
