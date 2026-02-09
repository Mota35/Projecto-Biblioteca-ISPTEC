package isptec.biblioteca.repository.impl;

import isptec.biblioteca.dao.EmprestimoDAO;
import isptec.biblioteca.model.entities.Emprestimo;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.model.entities.Membro;
import isptec.biblioteca.repository.EmprestimoRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do repositório de Empréstimo usando DAO.
 */
public class EmprestimoRepositoryImpl implements EmprestimoRepository {

    private final EmprestimoDAO emprestimoDAO;

    public EmprestimoRepositoryImpl() {
        this.emprestimoDAO = new EmprestimoDAO();
    }

    @Override
    public Emprestimo save(Emprestimo entity) {
        try {
            if (entity.getId() == 0) {
                // Insert
                int id = emprestimoDAO.insert(entity);
                if (id > 0) {
                    entity.setId(id);
                    return entity;
                }
            } else {
                // Update
                if (emprestimoDAO.update(entity)) {
                    return entity;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar empréstimo: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Emprestimo> findById(Integer id) {
        try {
            return Optional.ofNullable(emprestimoDAO.findById(id));
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimo por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Emprestimo> findAll() {
        try {
            return emprestimoDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            emprestimoDAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Erro ao deletar empréstimo: " + e.getMessage());
        }
    }

    @Override
    public void delete(Emprestimo emprestimo) {
        if (emprestimo != null) {
            deleteById(emprestimo.getId());
        }
    }

    @Override
    public void deleteAll() {
        try {
            List<Emprestimo> emprestimos = emprestimoDAO.findAll();
            for (Emprestimo emp : emprestimos) {
                emprestimoDAO.delete(emp.getId());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar todos os empréstimos: " + e.getMessage());
        }
    }

    @Override
    public long count() {
        try {
            return emprestimoDAO.findAll().size();
        } catch (SQLException e) {
            System.err.println("Erro ao contar empréstimos: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean existsById(Integer id) {
        try {
            return emprestimoDAO.findById(id) != null;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Emprestimo> findByMembro(Membro membro) {
        try {
            return emprestimoDAO.findByMembro(membro.getId());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos por membro: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Emprestimo> findByLivro(Livro livro) {
        try {
            return emprestimoDAO.findByLivro(livro.getId());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos por livro: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Emprestimo> findAtivos() {
        try {
            return emprestimoDAO.findAtivos();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos ativos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Emprestimo> findAtrasados() {
        try {
            return emprestimoDAO.findAtrasados();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos atrasados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Emprestimo> findAtivosByMembro(Membro membro) {
        try {
            return emprestimoDAO.findAtivosByMembro(membro.getId());
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos ativos por membro: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Emprestimo> findByDataDevolucaoPrevista(LocalDate data) {
        try {
            List<Emprestimo> todos = emprestimoDAO.findAtivos();
            List<Emprestimo> resultado = new ArrayList<>();
            for (Emprestimo emp : todos) {
                if (emp.getDataDevolucaoPrevista() != null &&
                    emp.getDataDevolucaoPrevista().equals(data)) {
                    resultado.add(emp);
                }
            }
            return resultado;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos por data de devolução: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Emprestimo> findByPeriodo(LocalDate inicio, LocalDate fim) {
        try {
            List<Emprestimo> todos = emprestimoDAO.findAll();
            List<Emprestimo> resultado = new ArrayList<>();
            for (Emprestimo emp : todos) {
                LocalDate dataEmp = emp.getDataEmprestimo();
                if (dataEmp != null &&
                    !dataEmp.isBefore(inicio) &&
                    !dataEmp.isAfter(fim)) {
                    resultado.add(emp);
                }
            }
            return resultado;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos por período: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public long countAtivos() {
        try {
            return emprestimoDAO.findAtivos().size();
        } catch (SQLException e) {
            System.err.println("Erro ao contar empréstimos ativos: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public long countAtrasados() {
        try {
            return emprestimoDAO.findAtrasados().size();
        } catch (SQLException e) {
            System.err.println("Erro ao contar empréstimos atrasados: " + e.getMessage());
            return 0;
        }
    }
}

