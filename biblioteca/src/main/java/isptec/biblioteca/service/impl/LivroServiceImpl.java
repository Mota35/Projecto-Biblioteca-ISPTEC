package isptec.biblioteca.service.impl;

import isptec.biblioteca.domain.entities.Livro;
import isptec.biblioteca.service.LivroService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LivroServiceImpl implements LivroService {

	private final List<Livro> livros = new ArrayList<>();

	@Override
	public void cadastrarLivro(Livro livro) {
		livros.add(livro);
	}

	@Override
	public void atualizarLivro(Livro livro) {
		removerLivro(livro.getIsbn());
		cadastrarLivro(livro);
	}

	@Override
	public void removerLivro(String isbn) {
		livros.removeIf(l -> l.getIsbn() != null && l.getIsbn().equals(isbn));
	}

	@Override
	public Livro buscarLivroPorIsbn(String isbn) {
		return livros.stream().filter(l -> l.getIsbn() != null && l.getIsbn().equals(isbn)).findFirst().orElse(null);
	}

	@Override
	public List<Livro> listarTodosOsLivros() {
		return new ArrayList<>(livros);
	}

	@Override
	public List<Livro> buscarPorTitulo(String titulo) {
		return livros.stream().filter(l -> l.getTitulo() != null && l.getTitulo().toLowerCase().contains(titulo.toLowerCase())).collect(Collectors.toList());
	}

	@Override
	public List<Livro> buscarPorAutor(String autor) {
		return livros.stream().filter(l -> l.getAutores() != null && l.getAutores().stream().anyMatch(a -> a.getNome().toLowerCase().contains(autor.toLowerCase()))).collect(Collectors.toList());
	}

	@Override
	public List<Livro> buscarPorISBN(String isbn) {
		Livro l = buscarLivroPorIsbn(isbn);
		List<Livro> res = new ArrayList<>();
		if (l != null) res.add(l);
		return res;
	}
}
