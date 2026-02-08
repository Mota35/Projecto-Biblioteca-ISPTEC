package isptec.biblioteca.service.impl;

import isptec.biblioteca.model.entities.Estudante;
import isptec.biblioteca.enumeracao.EstadoEmprestimo;
import isptec.biblioteca.service.EstudanteService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de gestão de estudantes.
 * Utiliza lista em memória (pode ser substituída por base de dados).
 */
public class EstudanteServiceImpl implements EstudanteService {

    private final List<Estudante> estudantes = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void cadastrarEstudante(Estudante estudante) {
        if (estudante != null) {
            if (estudante.getId() == 0) {
                estudante.setId(nextId++);
            }
            estudantes.add(estudante);
        }
    }

    @Override
    public boolean atualizarEstudante(Estudante estudante) {
        if (estudante == null || estudante.getMatricula() == null) {
            return false;
        }

        for (int i = 0; i < estudantes.size(); i++) {
            if (estudantes.get(i).getMatricula().equals(estudante.getMatricula())) {
                estudantes.set(i, estudante);
                return true;
            }
        }
        return false;
    }

    @Override
    public void removerEstudante(String matricula) {
        estudantes.removeIf(e -> e != null && matricula.equals(e.getMatricula()));
    }

    @Override
    public Estudante buscarEstudantePorMatricula(String matricula) {
        if (matricula == null) return null;
        return estudantes.stream()
                .filter(e -> e.getMatricula() != null && e.getMatricula().equals(matricula))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Estudante buscarEstudantePorId(int id) {
        return estudantes.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Estudante buscarEstudantePorEmail(String email) {
        if (email == null) return null;
        return estudantes.stream()
                .filter(e -> e.getEmail() != null && e.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Estudante> listarTodosOsEstudantes() {
        return new ArrayList<>(estudantes);
    }

    @Override
    public List<Estudante> listarEstudantesPorCurso(String curso) {
        if (curso == null || curso.isEmpty()) {
            return new ArrayList<>();
        }
        String cursoLower = curso.toLowerCase();
        return estudantes.stream()
                .filter(e -> e.getCurso() != null &&
                            e.getCurso().toLowerCase().contains(cursoLower))
                .collect(Collectors.toList());
    }

    @Override
    public List<Estudante> listarEstudantesPorAno(int anoLetivo) {
        return estudantes.stream()
                .filter(e -> e.getAnoLetivo() == anoLetivo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Estudante> listarEstudantesComEmprestimos() {
        return estudantes.stream()
                .filter(e -> e.getHistoricoEmprestimos().stream()
                        .anyMatch(emp -> emp.getEstado() == EstadoEmprestimo.ATIVO ||
                                        emp.getEstado() == EstadoEmprestimo.ATRASADO))
                .collect(Collectors.toList());
    }

    @Override
    public int contarEstudantes() {
        return estudantes.size();
    }
}
