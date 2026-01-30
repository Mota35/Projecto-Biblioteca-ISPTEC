package isptec.biblioteca.service.impl;

import isptec.biblioteca.domain.entities.Estudante;
import isptec.biblioteca.service.EstudanteService;
import java.util.ArrayList;
import java.util.List;

public class EstudanteServiceImpl implements EstudanteService {

    private final List<Estudante> estudantes = new ArrayList<>();

    @Override
    public void cadastrarEstudante(Estudante estudante) {
        estudantes.add(estudante);
    }

    @Override
    public void removerEstudante(String matricula) {
        estudantes.removeIf(e -> e != null && matricula.equals(e.getMatricula()));
    }

    @Override
    public Estudante buscarEstudantePorMatricula(String matricula) {
        return estudantes.stream().filter(e -> e.getMatricula() != null && e.getMatricula().equals(matricula)).findFirst().orElse(null);
    }

    @Override
    public List<Estudante> listarTodosOsEstudantes() {
        return new ArrayList<>(estudantes);
    }
}
