package main.java.isptec.biblioteca.service;

import main.java.isptec.biblioteca.domain.entities.Livro;
import java.util.List;

public interface LivroService {
    void cadastrarLivro(Livro livro);
    void removerLivro(String isbn);
    Livro buscarLivroPorIsbn(String isbn);
    List<Livro> listarTodosOsLivros();

}
