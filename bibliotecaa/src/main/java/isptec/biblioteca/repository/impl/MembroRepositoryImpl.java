package isptec.biblioteca.repository.impl;

import isptec.biblioteca.dao.MembroDAO;
import isptec.biblioteca.model.entities.Membro;
import isptec.biblioteca.repository.MembroRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do repositório de Membro usando DAO.
 */
public class MembroRepositoryImpl implements MembroRepository {

    private final MembroDAO membroDAO;

    public MembroRepositoryImpl() {
        this.membroDAO = new MembroDAO();
    }

    @Override
    public Membro save(Membro entity) {
        try {
            if (entity.getId() == 0) {
                // Insert
                int id = membroDAO.insert(entity);
                if (id > 0) {
                    entity.setId(id);
                    return entity;
                }
            } else {
                // Update
                if (membroDAO.update(entity)) {
                    return entity;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar membro: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Membro> findById(Integer id) {
        try {
            return Optional.ofNullable(membroDAO.findById(id));
        } catch (SQLException e) {
            System.err.println("Erro ao buscar membro por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Membro> findAll() {
        try {
            return membroDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erro ao listar membros: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            membroDAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Erro ao deletar membro: " + e.getMessage());
        }
    }

    @Override
    public void delete(Membro membro) {
        if (membro != null) {
            deleteById(membro.getId());
        }
    }

    @Override
    public void deleteAll() {
        try {
            List<Membro> membros = membroDAO.findAll();
            for (Membro membro : membros) {
                membroDAO.delete(membro.getId());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar todos os membros: " + e.getMessage());
        }
    }

    @Override
    public long count() {
        try {
            return membroDAO.findAll().size();
        } catch (SQLException e) {
            System.err.println("Erro ao contar membros: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean existsById(Integer id) {
        try {
            return membroDAO.findById(id) != null;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Membro findByMatricula(String matricula) {
        try {
            return membroDAO.findByMatricula(matricula);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar membro por matrícula: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Membro findByEmail(String email) {
        try {
            return membroDAO.findByEmail(email);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar membro por email: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Membro> findByNomeContaining(String nome) {
        try {
            List<Membro> todos = membroDAO.findAll();
            List<Membro> resultado = new ArrayList<>();
            String nomeLower = nome.toLowerCase();

            for (Membro membro : todos) {
                if (membro.getNome().toLowerCase().contains(nomeLower)) {
                    resultado.add(membro);
                }
            }
            return resultado;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar membros por nome: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Membro> findComEmprestimosAtivos() {
        try {
            return membroDAO.findAtivos();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar membros ativos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Membro> findBloqueados() {
        try {
            List<Membro> todos = membroDAO.findAll();
            List<Membro> bloqueados = new ArrayList<>();

            for (Membro membro : todos) {
                if (membro.isBloqueado()) {
                    bloqueados.add(membro);
                }
            }
            return bloqueados;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar membros bloqueados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Membro> findComMultasPendentes() {
        try {
            List<Membro> todos = membroDAO.findAll();
            List<Membro> comMultas = new ArrayList<>();

            for (Membro membro : todos) {
                if (membro.getMultaPendente() > 0) {
                    comMultas.add(membro);
                }
            }
            return comMultas;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar membros com multas pendentes: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

