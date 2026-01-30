package main.java.isptec.biblioteca.domain.entities;

import java.util.List;
import main.java.isptec.biblioteca.domain.enumeracao.EstadoLivro;

public class Livro {

    private int id;
    private String titulo;
    private String isbn;
    private String editora;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private List<Autor> autores;
    private Categoria categoria;
    private EstadoLivro estado;

    public Livro(int id, String titulo, String isbn, String editora, int quantidadeTotal, List<Autor> autores, Categoria categoria) {

        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.editora = editora;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal;
        this.autores = autores;
        this.categoria = categoria;
        this.estado = EstadoLivro.DISPONIVEL;
    }

    
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public void setQuantidadeTotal(int quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeTotalDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public EstadoLivro getEstado() {
        return estado;
    }

    public void setEstado(EstadoLivro estado) {
        this.estado = estado;
    }


}

