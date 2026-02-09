package isptec.biblioteca.service.impl;

import isptec.biblioteca.model.entities.Categoria;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.enumeracao.EstadoLivro;
import isptec.biblioteca.repository.LivroRepository;
import isptec.biblioteca.repository.impl.LivroRepositoryImpl;
import isptec.biblioteca.service.LivroService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de gestão de livros.
 * Utiliza banco de dados MySQL via repositório.
 */
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;

    public LivroServiceImpl() {
        this.livroRepository = new LivroRepositoryImpl();
    }

    @Override
    public void cadastrarLivro(Livro livro) {
        if (livro != null) {
            livroRepository.save(livro);
        }
    }

    @Override
    public void atualizarLivro(Livro livro) {
        if (livro != null) {
            livroRepository.save(livro);
        }
    }

    @Override
    public void removerLivro(String isbn) {
        Livro livro = buscarLivroPorIsbn(isbn);
        if (livro != null) {
            livroRepository.deleteById(livro.getId());
        }
    }

    @Override
    public void removerLivroPorId(int id) {
        livroRepository.deleteById(id);
    }

    @Override
    public Livro buscarLivroPorIsbn(String isbn) {
        if (isbn == null) return null;
        return livroRepository.findByIsbn(isbn);
    }

    @Override
    public Livro buscarLivroPorId(int id) {
        return livroRepository.findById(id).orElse(null);
    }

    @Override
    public List<Livro> listarTodosOsLivros() {
        return livroRepository.findAll();
    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isEmpty()) return new ArrayList<>();
        return livroRepository.findByTituloContaining(titulo);
    }

    @Override
    public List<Livro> buscarPorAutor(String autor) {
        if (autor == null || autor.isEmpty()) return new ArrayList<>();
        return livroRepository.findByAutor(autor);
    }

    @Override
    public List<Livro> buscarPorISBN(String isbn) {
        if (isbn == null || isbn.isEmpty()) return new ArrayList<>();
        Livro livro = buscarLivroPorIsbn(isbn);
        List<Livro> resultado = new ArrayList<>();
        if (livro != null) {
            resultado.add(livro);
        }
        return resultado;
    }

    @Override
    public List<Livro> buscarPorCategoria(Categoria categoria) {
        if (categoria == null) return new ArrayList<>();
        return buscarPorNomeCategoria(categoria.getNome());
    }

    @Override
    public List<Livro> buscarPorNomeCategoria(String nomeCategoria) {
        if (nomeCategoria == null || nomeCategoria.isEmpty()) return new ArrayList<>();
        return livroRepository.findByCategoria(nomeCategoria);
    }

    @Override
    public List<Livro> listarLivrosDisponiveis() {
        return livroRepository.findDisponiveis();
    }

    @Override
    public List<Livro> listarLivrosEmprestados() {
        return livroRepository.findAll().stream()
                .filter(l -> l.getEstado() == EstadoLivro.EMPRESTADO ||
                            l.getQuantidadeDisponivel() < l.getQuantidadeTotal())
                .collect(Collectors.toList());
    }

    @Override
    public int contarLivros() {
        return (int) livroRepository.count();
    }

    @Override
    public int contarLivrosDisponiveis() {
        return livroRepository.findDisponiveis().size();
    }
}
