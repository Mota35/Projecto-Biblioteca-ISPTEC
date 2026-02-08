package isptec.biblioteca.service.impl;

import isptec.biblioteca.model.entities.Categoria;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.enumeracao.EstadoLivro;
import isptec.biblioteca.service.LivroService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de gestão de livros.
 * Utiliza lista em memória (pode ser substituída por base de dados).
 */
public class LivroServiceImpl implements LivroService {

    private final List<Livro> livros = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void cadastrarLivro(Livro livro) {
        if (livro != null) {
            if (livro.getId() == 0) {
                livro.setId(nextId++);
            }
            livros.add(livro);
        }
    }

    @Override
    public void atualizarLivro(Livro livro) {
        if (livro != null && livro.getIsbn() != null) {
            removerLivro(livro.getIsbn());
            livros.add(livro);
        }
    }

    @Override
    public void removerLivro(String isbn) {
        livros.removeIf(l -> l.getIsbn() != null && l.getIsbn().equals(isbn));
    }

    @Override
    public void removerLivroPorId(int id) {
        livros.removeIf(l -> l.getId() == id);
    }

    @Override
    public Livro buscarLivroPorIsbn(String isbn) {
        if (isbn == null) return null;
        return livros.stream()
                .filter(l -> l.getIsbn() != null && l.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Livro buscarLivroPorId(int id) {
        return livros.stream()
                .filter(l -> l.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Livro> listarTodosOsLivros() {
        return new ArrayList<>(livros);
    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isEmpty()) return new ArrayList<>();
        String tituloLower = titulo.toLowerCase();
        return livros.stream()
                .filter(l -> l.getTitulo() != null &&
                            l.getTitulo().toLowerCase().contains(tituloLower))
                .collect(Collectors.toList());
    }

    @Override
    public List<Livro> buscarPorAutor(String autor) {
        if (autor == null || autor.isEmpty()) return new ArrayList<>();
        String autorLower = autor.toLowerCase();
        return livros.stream()
                .filter(l -> l.getAutores() != null &&
                            l.getAutores().stream()
                                .anyMatch(a -> a.getNome() != null &&
                                              a.getNome().toLowerCase().contains(autorLower)))
                .collect(Collectors.toList());
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
        return livros.stream()
                .filter(l -> l.getCategorias() != null &&
                            l.getCategorias().contains(categoria))
                .collect(Collectors.toList());
    }

    @Override
    public List<Livro> buscarPorNomeCategoria(String nomeCategoria) {
        if (nomeCategoria == null || nomeCategoria.isEmpty()) return new ArrayList<>();
        String catLower = nomeCategoria.toLowerCase();
        return livros.stream()
                .filter(l -> l.getCategorias() != null &&
                            l.getCategorias().stream()
                                .anyMatch(c -> c.getNome() != null &&
                                              c.getNome().toLowerCase().contains(catLower)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Livro> listarLivrosDisponiveis() {
        return livros.stream()
                .filter(Livro::estaDisponivel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Livro> listarLivrosEmprestados() {
        return livros.stream()
                .filter(l -> l.getEstado() == EstadoLivro.EMPRESTADO ||
                            l.getQuantidadeDisponivel() < l.getQuantidadeTotal())
                .collect(Collectors.toList());
    }

    @Override
    public int contarLivros() {
        return livros.size();
    }

    @Override
    public int contarLivrosDisponiveis() {
        return (int) livros.stream().filter(Livro::estaDisponivel).count();
    }
}
