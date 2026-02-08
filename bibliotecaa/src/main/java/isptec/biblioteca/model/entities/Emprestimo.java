package isptec.biblioteca.model.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import isptec.biblioteca.enumeracao.EstadoEmprestimo;

/**
 * Classe Emprestimo - Representa um empréstimo de livro.
 *
 * REGRAS DE NEGÓCIO:
 * - Prazo padrão: 14 dias
 * - Máximo 2 renovações
 * - Não pode renovar se existir reserva ativa para o livro
 * - Empréstimo atrasado gera multa diária
 */
public class Emprestimo {

    // Constantes de regras de negócio
    public static final int PRAZO_PADRAO_DIAS = 14;
    public static final int MAX_RENOVACOES = 2;
    public static final double MULTA_DIARIA = 50.0; // KZ por dia de atraso

    private int id;
    private Livro livro;
    private Membro membro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private int numeroRenovacoes;
    private EstadoEmprestimo estado;

    public Emprestimo() {
        this.id = 0;
        this.livro = null;
        this.membro = null;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = LocalDate.now().plusDays(PRAZO_PADRAO_DIAS);
        this.dataDevolucaoReal = null;
        this.numeroRenovacoes = 0;
        this.estado = EstadoEmprestimo.ATIVO;
    }

    public Emprestimo(int id, Livro livro, Membro membro) {
        this.id = id;
        this.livro = livro;
        this.membro = membro;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = LocalDate.now().plusDays(PRAZO_PADRAO_DIAS);
        this.dataDevolucaoReal = null;
        this.numeroRenovacoes = 0;
        this.estado = EstadoEmprestimo.ATIVO;
    }

    public Emprestimo(int id, Livro livro, Membro membro, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
        this.id = id;
        this.livro = livro;
        this.membro = membro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = null;
        this.numeroRenovacoes = 0;
        this.estado = EstadoEmprestimo.ATIVO;
    }

    // Construtor de compatibilidade com Estudante
    public Emprestimo(int id, Livro livro, Estudante estudante, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
        this(id, livro, (Membro) estudante, dataEmprestimo, dataDevolucaoPrevista);
    }

    // === MÉTODOS DE NEGÓCIO ===

    /**
     * Verifica se o empréstimo está atrasado.
     *
     * @return true se a data atual passou da data prevista de devolução e não foi devolvido
     */
    public boolean estaAtrasado() {
        if (estado == EstadoEmprestimo.DEVOLVIDO) {
            return false;
        }
        return LocalDate.now().isAfter(dataDevolucaoPrevista);
    }

    /**
     * Calcula os dias de atraso.
     *
     * @return número de dias de atraso (0 se não estiver atrasado)
     */
    public long getDiasAtraso() {
        if (!estaAtrasado()) {
            return 0;
        }
        LocalDate dataRef = dataDevolucaoReal != null ? dataDevolucaoReal : LocalDate.now();
        return ChronoUnit.DAYS.between(dataDevolucaoPrevista, dataRef);
    }

    /**
     * Calcula a multa por atraso.
     *
     * REGRA: Multa diária de MULTA_DIARIA KZ
     *
     * @return valor da multa em KZ
     */
    public double calcularMulta() {
        long diasAtraso = getDiasAtraso();
        if (diasAtraso <= 0) {
            return 0.0;
        }
        return diasAtraso * MULTA_DIARIA;
    }

    /**
     * Tenta renovar o empréstimo.
     *
     * REGRAS:
     * - Máximo 2 renovações
     * - Não pode renovar se estiver atrasado
     *
     * @return true se a renovação foi bem-sucedida
     */
    public boolean renovar() {
        if (numeroRenovacoes >= MAX_RENOVACOES) {
            return false;
        }
        if (estaAtrasado()) {
            return false;
        }
        if (estado != EstadoEmprestimo.ATIVO) {
            return false;
        }

        numeroRenovacoes++;
        dataDevolucaoPrevista = dataDevolucaoPrevista.plusDays(PRAZO_PADRAO_DIAS);
        return true;
    }

    /**
     * Tenta renovar o empréstimo com uma nova data específica.
     *
     * @param novaDataDevolucao a nova data de devolução prevista
     * @return true se a renovação foi bem-sucedida
     */
    public boolean renovar(LocalDate novaDataDevolucao) {
        if (numeroRenovacoes >= MAX_RENOVACOES) {
            return false;
        }
        if (estado != EstadoEmprestimo.ATIVO) {
            return false;
        }

        numeroRenovacoes++;
        dataDevolucaoPrevista = novaDataDevolucao;
        return true;
    }

    /**
     * Verifica se ainda pode renovar.
     *
     * @return true se ainda há renovações disponíveis
     */
    public boolean podeRenovar() {
        return numeroRenovacoes < MAX_RENOVACOES &&
               estado == EstadoEmprestimo.ATIVO &&
               !estaAtrasado();
    }

    /**
     * Registra a devolução do livro.
     *
     * @param dataDevolucao a data da devolução
     */
    public void registrarDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucaoReal = dataDevolucao;
        this.estado = EstadoEmprestimo.DEVOLVIDO;
    }

    /**
     * Registra a devolução do livro com a data atual.
     */
    public void registrarDevolucao() {
        registrarDevolucao(LocalDate.now());
    }

    /**
     * Atualiza o estado do empréstimo verificando se está atrasado.
     */
    public void atualizarEstado() {
        if (estado == EstadoEmprestimo.DEVOLVIDO) {
            return;
        }
        if (estaAtrasado()) {
            estado = EstadoEmprestimo.ATRASADO;
        }
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

    // Método de compatibilidade
    public Estudante getEstudante() {
        if (membro instanceof Estudante) {
            return (Estudante) membro;
        }
        return null;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    // Alias para compatibilidade
    public LocalDate getDataPrevistaDevolucao() {
        return dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    // Alias para compatibilidade
    public LocalDate getDataRealDevolucao() {
        return dataDevolucaoReal;
    }

    public void setDataRealDevolucao(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public int getNumeroRenovacoes() {
        return numeroRenovacoes;
    }

    public void setNumeroRenovacoes(int numeroRenovacoes) {
        this.numeroRenovacoes = numeroRenovacoes;
    }

    public void incrementarRenovacoes() {
        this.numeroRenovacoes++;
    }

    public EstadoEmprestimo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEmprestimo estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Emprestimo{id=" + id +
               ", livro='" + (livro != null ? livro.getTitulo() : "N/A") + "'" +
               ", membro='" + (membro != null ? membro.getNome() : "N/A") + "'" +
               ", dataEmprestimo=" + dataEmprestimo +
               ", dataDevolucaoPrevista=" + dataDevolucaoPrevista +
               ", estado=" + estado +
               ", renovacoes=" + numeroRenovacoes + "/" + MAX_RENOVACOES +
               (estaAtrasado() ? ", ATRASADO " + getDiasAtraso() + " dias, multa=" + calcularMulta() + " KZ" : "") +
               "}";
    }
}
