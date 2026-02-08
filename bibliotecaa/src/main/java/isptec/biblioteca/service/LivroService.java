package isptec.biblioteca.service;

import isptec.biblioteca.model.entities.Categoria;
import isptec.biblioteca.model.entities.Livro;
import java.util.List;

/**
 * Interface de serviço para gestão de livros.
 * Define as operações de negócio relacionadas a livros.
 */
public interface LivroService {

    /**
     * Cadastra um novo livro no sistema.
     *
     * @param livro o livro a cadastrar
     */
    void cadastrarLivro(Livro livro);

    /**
     * Atualiza os dados de um livro existente.
     *
     * @param livro o livro com dados atualizados
     */
    void atualizarLivro(Livro livro);

    /**
     * Remove um livro pelo ISBN.
     *
     * @param isbn o ISBN do livro
     */
    void removerLivro(String isbn);

    /**
     * Remove um livro pelo ID.
     *
     * @param id o ID do livro
     */
    void removerLivroPorId(int id);

    /**
     * Busca um livro pelo ISBN.
     *
     * @param isbn o ISBN do livro
     * @return o livro encontrado ou null
     */
    Livro buscarLivroPorIsbn(String isbn);

    /**
     * Busca um livro pelo ID.
     *
     * @param id o ID do livro
     * @return o livro encontrado ou null
     */
    Livro buscarLivroPorId(int id);

    /**
     * Lista todos os livros.
     *
     * @return lista de todos os livros
     */
    List<Livro> listarTodosOsLivros();

    /**
     * Busca livros pelo título (busca parcial).
     *
     * @param titulo o título ou parte dele
     * @return lista de livros encontrados
     */
    List<Livro> buscarPorTitulo(String titulo);

    /**
     * Busca livros por autor.
     *
     * @param autor o nome do autor
     * @return lista de livros do autor
     */
    List<Livro> buscarPorAutor(String autor);

    /**
     * Busca livros por ISBN.
     *
     * @param isbn o ISBN do livro
     * @return lista de livros encontrados
     */
    List<Livro> buscarPorISBN(String isbn);

    /**
     * Busca livros por categoria.
     *
     * @param categoria a categoria
     * @return lista de livros da categoria
     */
    List<Livro> buscarPorCategoria(Categoria categoria);

    /**
     * Busca livros pelo nome da categoria.
     *
     * @param nomeCategoria o nome da categoria
     * @return lista de livros da categoria
     */
    List<Livro> buscarPorNomeCategoria(String nomeCategoria);

    /**
     * Lista apenas os livros disponíveis.
     *
     * @return lista de livros disponíveis
     */
    List<Livro> listarLivrosDisponiveis();

    /**
     * Lista os livros emprestados.
     *
     * @return lista de livros emprestados
     */
    List<Livro> listarLivrosEmprestados();

    /**
     * Conta o total de livros no sistema.
     *
     * @return número total de livros
     */
    int contarLivros();

    /**
     * Conta livros disponíveis.
     *
     * @return número de livros disponíveis
     */
    int contarLivrosDisponiveis();
}
