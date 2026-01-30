package isptec.biblioteca.service;

import java.util.List;

import isptec.biblioteca.domain.entities.Estudante;

public interface EstudanteService {
    void cadastrarEstudante(Estudante estudante);
    void removerEstudante(String matricula);
    Estudante buscarEstudantePorMatricula(String matricula);
    List<Estudante> listarTodosOsEstudantes();

}
