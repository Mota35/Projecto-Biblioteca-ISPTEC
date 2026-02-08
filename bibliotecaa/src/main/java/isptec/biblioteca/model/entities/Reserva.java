package isptec.biblioteca.model.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Classe Reserva - Representa uma reserva de livro por um membro.
 *
 * REGRAS DE NEGÓCIO:
 * - Só é possível reservar livro indisponível
 * - Reservas são atendidas por ordem de data
 * - Reserva expira após 3 dias se não for retirada
 * - Membro não pode ter múltiplas reservas ativas para o mesmo livro
 */
public class Reserva {

    // Constantes de regras de negócio
    public static final int DIAS_EXPIRACAO = 3;

    private int id;
    private Livro livro;
    private Membro membro;
    private LocalDate dataReserva;
    private LocalDate dataExpiracao;
    private boolean ativa;
    private boolean confirmada;
    private boolean notificado;

    public Reserva() {
        this.id = 0;
        this.livro = null;
        this.membro = null;
        this.dataReserva = LocalDate.now();
        this.dataExpiracao = LocalDate.now().plusDays(DIAS_EXPIRACAO);
        this.ativa = true;
        this.confirmada = false;
        this.notificado = false;
    }

    public Reserva(int id, Livro livro, Membro membro) {
        this.id = id;
        this.livro = livro;
        this.membro = membro;
        this.dataReserva = LocalDate.now();
        this.dataExpiracao = LocalDate.now().plusDays(DIAS_EXPIRACAO);
        this.ativa = true;
        this.confirmada = false;
        this.notificado = false;
    }

    public Reserva(int id, Livro livro, Membro membro, LocalDate dataReserva) {
        this.id = id;
        this.livro = livro;
        this.membro = membro;
        this.dataReserva = dataReserva;
        this.dataExpiracao = dataReserva.plusDays(DIAS_EXPIRACAO);
        this.ativa = true;
        this.confirmada = false;
        this.notificado = false;
    }

    // Construtor de compatibilidade com Estudante
    public Reserva(int id, Livro livro, Estudante estudante, LocalDate dataReserva) {
        this(id, livro, (Membro) estudante, dataReserva);
    }

    // === MÉTODOS DE NEGÓCIO ===

    /**
     * Verifica se a reserva está expirada.
     *
     * @return true se passou da data de expiração
     */
    public boolean estaExpirada() {
        if (!ativa) {
            return false;
        }
        return LocalDate.now().isAfter(dataExpiracao);
    }

    /**
     * Calcula os dias restantes para a reserva expirar.
     *
     * @return número de dias restantes (pode ser negativo se expirada)
     */
    public long getDiasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataExpiracao);
    }

    /**
     * Cancela a reserva.
     */
    public void cancelar() {
        this.ativa = false;
    }

    /**
     * Confirma a reserva (membro retirou o livro).
     */
    public void confirmar() {
        this.confirmada = true;
        this.ativa = false;
    }

    /**
     * Verifica se a reserva pode ser atendida.
     *
     * @return true se está ativa e não expirada
     */
    public boolean podeSerAtendida() {
        return ativa && !estaExpirada() && !confirmada;
    }

    /**
     * Verifica se a reserva está válida (ativa e não expirada).
     *
     * @return true se está válida
     */
    public boolean estaValida() {
        return ativa && !estaExpirada() && !confirmada;
    }

    /**
     * Verifica se o membro já foi notificado sobre a disponibilidade.
     *
     * @return true se já foi notificado
     */
    public boolean isNotificado() {
        return notificado;
    }

    /**
     * Marca a reserva como notificada.
     */
    public void marcarNotificado() {
        this.notificado = true;
    }

    // === GETTERS E SETTERS ===

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Membro getMembro() {
        return membro;
    }

    public void setMembro(Membro membro) {
        this.membro = membro;
    }

    // Método de compatibilidade com código antigo
    public Estudante getEstudante() {
        if (membro instanceof Estudante) {
            return (Estudante) membro;
        }
        return null;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
        this.dataExpiracao = dataReserva.plusDays(DIAS_EXPIRACAO);
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return id == reserva.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reserva{id=" + id +
               ", livro='" + (livro != null ? livro.getTitulo() : "N/A") + "'" +
               ", membro='" + (membro != null ? membro.getNome() : "N/A") + "'" +
               ", dataReserva=" + dataReserva +
               ", ativa=" + ativa +
               ", expirada=" + estaExpirada() +
               "}";
    }
}
