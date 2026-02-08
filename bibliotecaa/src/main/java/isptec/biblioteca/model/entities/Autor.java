package isptec.biblioteca.model.entities;

import java.util.Objects;

/**
 * Classe Autor - Representa um autor de livros.
 */
public class Autor {

    private int id;
    private String nome;
    private String nacionalidade;
    private String biografia;

    public Autor() {
        this.id = 0;
        this.nome = "";
        this.nacionalidade = "";
        this.biografia = "";
    }

    public Autor(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.nacionalidade = "";
        this.biografia = "";
    }

    public Autor(int id, String nome, String nacionalidade) {
        this.id = id;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.biografia = "";
    }

    public Autor(int id, String nome, String nacionalidade, String biografia) {
        this.id = id;
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.biografia = biografia;
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

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return id == autor.id || Objects.equals(nome, autor.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "Autor{id=" + id + ", nome='" + nome + "'" +
               (nacionalidade != null && !nacionalidade.isEmpty() ? ", nacionalidade='" + nacionalidade + "'" : "") +
               "}" +
               (biografia != null && !biografia.isEmpty() ? ", biografia='" + biografia + "'" : "");
    }
}
