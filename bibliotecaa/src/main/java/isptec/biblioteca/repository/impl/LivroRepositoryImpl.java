package isptec.biblioteca.repository.impl;

import isptec.biblioteca.dao.LivroDAO;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.repository.LivroRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do repositório de Livro usando DAO.
 */
public class LivroRepositoryImpl implements LivroRepository {

    private final LivroDAO livroDAO;

    public LivroRepositoryImpl() {
        this.livroDAO = new LivroDAO();
    }

    @Override
    public Livro save(Livro entity) {
        try {
            if (entity.getId() == 0) {
                // Insert
                int id = livroDAO.insert(entity);
                if (id > 0) {
                    entity.setId(id);
                    return entity;
                }
            } else {
                // Update
                if (livroDAO.update(entity)) {
                    return entity;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar livro: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Livro> findById(Integer id) {
        try {
            return Optional.ofNullable(livroDAO.findById(id));
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Livro> findAll() {
        try {
            return livroDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erro ao listar livros: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            livroDAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Erro ao deletar livro: " + e.getMessage());
        }
    }

    @Override
    public void delete(Livro livro) {
        if (livro != null) {
            deleteById(livro.getId());
        }
    }

    @Override
    public void deleteAll() {
        try {
            List<Livro> livros = livroDAO.findAll();
            for (Livro livro : livros) {
                livroDAO.delete(livro.getId());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar todos os livros: " + e.getMessage());
        }
    }

    @Override
    public long count() {
        try {
            return livroDAO.findAll().size();
        } catch (SQLException e) {
            System.err.println("Erro ao contar livros: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean existsById(Integer id) {
        try {
            return livroDAO.findById(id) != null;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Livro findByIsbn(String isbn) {
        try {
            return livroDAO.findByIsbn(isbn);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro por ISBN: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Livro> findByTituloContaining(String titulo) {
        try {
            return livroDAO.findByTituloContaining(titulo);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros por título: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Livro> findByAutor(String nomeAutor) {
        try {
            return livroDAO.findByAutor(nomeAutor);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros por autor: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Livro> findByCategoria(String nomeCategoria) {
        try {
            return livroDAO.findByCategoria(nomeCategoria);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros por categoria: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Livro> findDisponiveis() {
        try {
            return livroDAO.findDisponiveis();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros disponíveis: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Livro> findByEditora(String editora) {
        try {
            List<Livro> todos = livroDAO.findAll();
            List<Livro> resultado = new ArrayList<>();
            String editoraLower = editora.toLowerCase();

            for (Livro livro : todos) {
                if (livro.getEditora() != null &&
                    livro.getEditora().toLowerCase().contains(editoraLower)) {
                    resultado.add(livro);
                }
            }
            return resultado;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros por editora: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Livro> findByAno(int ano) {
        try {
            List<Livro> todos = livroDAO.findAll();
            List<Livro> resultado = new ArrayList<>();

            for (Livro livro : todos) {
                if (livro.getAnoPublicacao() == ano) {
                    resultado.add(livro);
                }
            }
            return resultado;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros por ano: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Livro> findEmprestados() {
        try {
            List<Livro> todos = livroDAO.findAll();
            List<Livro> resultado = new ArrayList<>();

            for (Livro livro : todos) {
                if (livro.getQuantidadeDisponivel() < livro.getQuantidadeTotal()) {
                    resultado.add(livro);
                }
            }
            return resultado;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros emprestados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public long countDisponiveis() {
        try {
            return livroDAO.findDisponiveis().size();
        } catch (SQLException e) {
            System.err.println("Erro ao contar livros disponíveis: " + e.getMessage());
            return 0;
        }
    }
}

