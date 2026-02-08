package isptec.biblioteca.repository;

import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.model.entities.Membro;
import isptec.biblioteca.model.entities.Reserva;
import java.util.List;

/**
 * Interface de repositório específica para Reserva.
 */
public interface ReservaRepository extends Repository<Reserva, Integer> {

    /**
     * Busca reservas de um membro.
     *
     * @param membro o membro
     * @return lista de reservas do membro
     */
    List<Reserva> findByMembro(Membro membro);

    /**
     * Busca reservas de um livro.
     *
     * @param livro o livro
     * @return lista de reservas do livro
     */
    List<Reserva> findByLivro(Livro livro);

    /**
     * Lista reservas ativas.
     *
     * @return lista de reservas ativas
     */
    List<Reserva> findAtivas();

    /**
     * Lista reservas ativas para um livro (ordenadas por data).
     *
     * @param livro o livro
     * @return lista de reservas ativas do livro
     */
    List<Reserva> findAtivasByLivro(Livro livro);

    /**
     * Lista reservas ativas de um membro.
     *
     * @param membro o membro
     * @return lista de reservas ativas do membro
     */
    List<Reserva> findAtivasByMembro(Membro membro);

    /**
     * Lista reservas expiradas.
     *
     * @return lista de reservas expiradas
     */
    List<Reserva> findExpiradas();

    /**
     * Verifica se existe reserva ativa do membro para o livro.
     *
     * @param membro o membro
     * @param livro o livro
     * @return true se existe reserva ativa
     */
    boolean existsAtivaByMembroAndLivro(Membro membro, Livro livro);

    /**
     * Conta reservas ativas.
     *
     * @return número de reservas ativas
     */
    long countAtivas();
}

