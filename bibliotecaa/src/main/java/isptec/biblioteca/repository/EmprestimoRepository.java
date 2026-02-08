package isptec.biblioteca.repository;

import isptec.biblioteca.model.entities.Emprestimo;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.model.entities.Membro;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface de repositório específica para Emprestimo.
 */
public interface EmprestimoRepository extends Repository<Emprestimo, Integer> {

    /**
     * Busca empréstimos de um membro.
     *
     * @param membro o membro
     * @return lista de empréstimos do membro
     */
    List<Emprestimo> findByMembro(Membro membro);

    /**
     * Busca empréstimos de um livro.
     *
     * @param livro o livro
     * @return lista de empréstimos do livro
     */
    List<Emprestimo> findByLivro(Livro livro);

    /**
     * Lista empréstimos ativos (não devolvidos).
     *
     * @return lista de empréstimos ativos
     */
    List<Emprestimo> findAtivos();

    /**
     * Lista empréstimos atrasados.
     *
     * @return lista de empréstimos atrasados
     */
    List<Emprestimo> findAtrasados();

    /**
     * Lista empréstimos que vencem em uma data.
     *
     * @param data a data de vencimento
     * @return lista de empréstimos que vencem na data
     */
    List<Emprestimo> findByDataDevolucaoPrevista(LocalDate data);

    /**
     * Lista empréstimos realizados em um período.
     *
     * @param inicio data inicial
     * @param fim data final
     * @return lista de empréstimos no período
     */
    List<Emprestimo> findByPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Lista empréstimos ativos de um membro.
     *
     * @param membro o membro
     * @return lista de empréstimos ativos do membro
     */
    List<Emprestimo> findAtivosByMembro(Membro membro);

    /**
     * Conta empréstimos ativos.
     *
     * @return número de empréstimos ativos
     */
    long countAtivos();

    /**
     * Conta empréstimos atrasados.
     *
     * @return número de empréstimos atrasados
     */
    long countAtrasados();
}

