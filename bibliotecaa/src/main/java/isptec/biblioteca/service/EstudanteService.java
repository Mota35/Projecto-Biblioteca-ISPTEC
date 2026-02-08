package isptec.biblioteca.service;

import isptec.biblioteca.model.entities.Estudante;
import java.util.List;

/**
 * Interface de serviço para gestão de estudantes.
 * Estudante é um tipo específico de Membro.
 */
public interface EstudanteService {

    /**
     * Cadastra um novo estudante no sistema.
     */
    void cadastrarEstudante(Estudante estudante);

    /**
     * Atualiza os dados de um estudante existente.
     * @return true se atualizado com sucesso
     */
    boolean atualizarEstudante(Estudante estudante);

    /**
     * Remove um estudante pela matrícula.
     */
    void removerEstudante(String matricula);

    /**
     * Busca um estudante pela matrícula.
     */
    Estudante buscarEstudantePorMatricula(String matricula);

    /**
     * Busca um estudante pelo ID.
     */
    Estudante buscarEstudantePorId(int id);

    /**
     * Busca um estudante pelo email.
     */
    Estudante buscarEstudantePorEmail(String email);

    /**
     * Lista todos os estudantes.
     */
    List<Estudante> listarTodosOsEstudantes();

    /**
     * Lista estudantes por curso.
     */
    List<Estudante> listarEstudantesPorCurso(String curso);

    /**
     * Lista estudantes por ano letivo.
     */
    List<Estudante> listarEstudantesPorAno(int anoLetivo);

    /**
     * Lista estudantes com empréstimos ativos.
     */
    List<Estudante> listarEstudantesComEmprestimos();

    /**
     * Conta o total de estudantes.
     */
    int contarEstudantes();
}
