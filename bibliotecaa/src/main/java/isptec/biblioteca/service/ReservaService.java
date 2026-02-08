package isptec.biblioteca.service;

import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.model.entities.Membro;
import isptec.biblioteca.model.entities.Reserva;
import java.util.List;

/**
 * Interface de serviço para gestão de reservas.
 *
 * REGRAS DE NEGÓCIO:
 * - Só é possível reservar livro indisponível
 * - Reservas são atendidas por ordem de data
 * - Reserva expira após 3 dias se não for retirada
 * - Membro não pode ter múltiplas reservas ativas para o mesmo livro
 */
public interface ReservaService {

    /**
     * Reserva um livro para um membro.
     */
    boolean reservarLivro(Membro membro, Livro livro);

    /**
     * Cancela uma reserva pelo ID.
     */
    void cancelarReserva(int reservaId);

    /**
     * Cancela uma reserva.
     */
    void cancelarReserva(Reserva reserva);

    /**
     * Confirma uma reserva (membro retirou o livro).
     */
    void confirmarReserva(int reservaId);

    /**
     * Busca uma reserva pelo ID.
     */
    Reserva buscarReservaPorId(int reservaId);

    /**
     * Lista reservas ativas.
     */
    List<Reserva> listarReservasAtivas();

    /**
     * Lista todas as reservas.
     */
    List<Reserva> listarReservas();

    /**
     * Lista reservas de um livro.
     */
    List<Reserva> listarReservasLivro(Livro livro);

    /**
     * Lista reservas ativas de um livro.
     */
    List<Reserva> listarReservasAtivasLivro(Livro livro);

    /**
     * Lista reservas de um membro.
     */
    List<Reserva> listarReservasMembro(Membro membro);

    /**
     * Lista reservas ativas de um membro.
     */
    List<Reserva> listarReservasAtivasMembro(Membro membro);

    /**
     * Verifica se existe reserva ativa para um livro.
     */
    boolean existeReservaAtivaParaLivro(Livro livro);

    /**
     * Verifica se um membro já tem reserva ativa para um livro.
     */
    boolean membroTemReservaAtivaParaLivro(Membro membro, Livro livro);

    /**
     * Obtém a próxima reserva da fila para um livro.
     */
    Reserva obterProximaReservaFila(Livro livro);

    /**
     * Processa reservas expiradas (cancela automaticamente).
     */
    void processarReservasExpiradas();

    /**
     * Notifica membros quando seus livros reservados estão disponíveis.
     */
    void notificarDisponibilidade();

    /**
     * Conta reservas ativas.
     */
    int contarReservasAtivas();
}
