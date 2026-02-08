package isptec.biblioteca.service;

import isptec.biblioteca.model.entities.Membro;
import java.util.List;

/**
 * Interface de serviço para gestão de membros.
 * Define as operações de negócio relacionadas a membros.
 */
public interface MembroService {

    /**
     * Cadastra um novo membro no sistema.
     * @return true se cadastrado com sucesso
     */
    boolean cadastrarMembro(Membro membro);

    /**
     * Atualiza os dados de um membro existente.
     * @return true se atualizado com sucesso
     */
    boolean atualizarMembro(Membro membro);

    /**
     * Remove um membro pela matrícula.
     * @return true se removido com sucesso
     */
    boolean removerMembro(String matricula);

    /**
     * Busca um membro pela matrícula.
     */
    Membro buscarPorMatricula(String matricula);

    /**
     * Busca um membro pelo ID.
     */
    Membro buscarPorId(int id);

    /**
     * Lista todos os membros.
     */
    List<Membro> listarMembros();

    /**
     * Lista membros com empréstimos ativos.
     */
    List<Membro> listarMembrosComEmprestimosAtivos();

    /**
     * Lista membros bloqueados.
     */
    List<Membro> listarMembrosBloqueados();

    /**
     * Bloqueia um membro.
     * @return true se bloqueado com sucesso
     */
    boolean bloquearMembro(String matricula);

    /**
     * Desbloqueia um membro.
     * @return true se desbloqueado com sucesso
     */
    boolean desbloquearMembro(String matricula);

    /**
     * Verifica se um membro pode realizar empréstimo.
     * @return true se pode emprestar
     */
    boolean podeEmprestar(String matricula);
}
