package isptec.biblioteca.repository;

import isptec.biblioteca.model.entities.Livro;
import java.util.List;

/**
 * Interface de repositório específica para Livro.
 */
public interface LivroRepository extends Repository<Livro, Integer> {

    /**
     * Busca livro pelo ISBN.
     *
     * @param isbn o ISBN do livro
     * @return o livro ou null se não encontrado
     */
    Livro findByIsbn(String isbn);

    /**
     * Busca livros pelo título (parcial).
     *
     * @param titulo o título ou parte dele
     * @return lista de livros que correspondem
     */
    List<Livro> findByTituloContaining(String titulo);

    /**
     * Busca livros por autor.
     *
     * @param nomeAutor o nome do autor
     * @return lista de livros do autor
     */
    List<Livro> findByAutor(String nomeAutor);

    /**
     * Busca livros por categoria.
     *
     * @param nomeCategoria o nome da categoria
     * @return lista de livros da categoria
     */
    List<Livro> findByCategoria(String nomeCategoria);

    /**
     * Lista livros disponíveis para empréstimo.
     *
     * @return lista de livros disponíveis
     */
    List<Livro> findDisponiveis();

    /**
     * Lista livros emprestados.
     *
     * @return lista de livros emprestados
     */
    List<Livro> findEmprestados();

    /**
     * Conta livros disponíveis.
     *
     * @return número de livros disponíveis
     */
    long countDisponiveis();
}

