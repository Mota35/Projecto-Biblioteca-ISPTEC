package isptec.biblioteca.service;

import isptec.biblioteca.domain.entities.Livro;
import java.util.List;

public interface LivroService {
    void cadastrarLivro(Livro livro);
    void atualizarLivro(Livro livro);
    void removerLivro(String isbn);
    Livro buscarLivroPorIsbn(String isbn);
    List<Livro> listarTodosOsLivros();
    List<Livro> buscarPorTitulo(String titulo);
    List<Livro> buscarPorAutor(String autor);
    List<Livro> buscarPorISBN(String isbn);

}

    