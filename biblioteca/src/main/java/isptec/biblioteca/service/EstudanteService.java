package main.java.isptec.biblioteca.service;

import main.java.isptec.biblioteca.domain.entities.Estudante;

public interface EstudanteService {
    void cadastrarEstudante(Estudante estudante);
    void removerEstudante(String matricula);
    Estudante buscarEstudantePorMatricula(String matricula);
    List<Estudante> listarTodosOsEstudantes();

}
