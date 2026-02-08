package isptec.biblioteca.repository;

import isptec.biblioteca.model.entities.Membro;
import java.util.List;

/**
 * Interface de repositório específica para Membro.
 */
public interface MembroRepository extends Repository<Membro, Integer> {

    /**
     * Busca membro pela matrícula.
     *
     * @param matricula a matrícula do membro
     * @return o membro ou null se não encontrado
     */
    Membro findByMatricula(String matricula);

    /**
     * Busca membro pelo email.
     *
     * @param email o email do membro
     * @return o membro ou null se não encontrado
     */
    Membro findByEmail(String email);

    /**
     * Busca membros pelo nome (parcial).
     *
     * @param nome o nome ou parte dele
     * @return lista de membros que correspondem
     */
    List<Membro> findByNomeContaining(String nome);

    /**
     * Lista membros com empréstimos ativos.
     *
     * @return lista de membros com empréstimos
     */
    List<Membro> findComEmprestimosAtivos();

    /**
     * Lista membros bloqueados.
     *
     * @return lista de membros bloqueados
     */
    List<Membro> findBloqueados();

    /**
     * Lista membros com multas pendentes.
     *
     * @return lista de membros com multas
     */
    List<Membro> findComMultasPendentes();
}

