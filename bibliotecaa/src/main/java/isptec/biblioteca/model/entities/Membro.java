package isptec.biblioteca.model.entities;

import isptec.biblioteca.enumeracao.EstadoEmprestimo;
import isptec.biblioteca.enumeracao.Perfil;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Membro - Representa um membro da biblioteca (estudante/utilizador).
 * Herda de Pessoa e contém lógica de empréstimos.
 *
 * REGRAS DE NEGÓCIO:
 * - Um membro pode ter máximo 3 livros emprestados
 * - Membro bloqueado não pode emprestar
 * - Multas podem bloquear novos empréstimos
 */
public class Membro extends Pessoa {

    // Constantes de regras de negócio
    public static final int MAX_EMPRESTIMOS_ATIVOS = 3;
    public static final double MULTA_MAXIMA_PERMITIDA = 1000.0; // KZ para bloquear

    private String matricula;
    private List<Emprestimo> historicoEmprestimos;
    private boolean bloqueado;
    private double multaPendente;

    public Membro() {
        super();
        this.matricula = "";
        this.historicoEmprestimos = new ArrayList<>();
        this.bloqueado = false;
        this.multaPendente = 0.0;
    }

    public Membro(int id, String nome, String email, String matricula) {
        super(id, nome, email, Perfil.USUARIO);
        this.matricula = matricula;
        this.historicoEmprestimos = new ArrayList<>();
        this.bloqueado = false;
        this.multaPendente = 0.0;
    }

    // === MÉTODOS DE NEGÓCIO ===

    /**
     * Verifica se o membro pode realizar um novo empréstimo.
     * REGRAS:
     * - Não pode estar bloqueado
     * - Não pode ter mais de 3 empréstimos ativos
     * - Não pode ter multa acima do limite
     *
     * @return true se pode emprestar, false caso contrário
     */
    public boolean podeEmprestar() {
        if (bloqueado) {
            return false;
        }
        if (multaPendente > MULTA_MAXIMA_PERMITIDA) {
            return false;
        }
        return numeroEmprestimosAtivos() < MAX_EMPRESTIMOS_ATIVOS;
    }

    /**
     * Conta o número de empréstimos ativos do membro.
     *
     * @return número de empréstimos com estado ATIVO
     */
    public int numeroEmprestimosAtivos() {
        int count = 0;
        for (Emprestimo emp : historicoEmprestimos) {
            if (emp.getEstado() == EstadoEmprestimo.ATIVO) {
                count++;
            }
        }
        return count;
    }

    /**
     * Adiciona um empréstimo ao histórico do membro.
     *
     * @param emprestimo o empréstimo a adicionar
     */
    public void adicionarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo != null) {
            historicoEmprestimos.add(emprestimo);
        }
    }

    /**
     * Obtém lista de empréstimos ativos (não devolvidos).
     *
     * @return lista de empréstimos ativos
     */
    public List<Emprestimo> getEmprestimosAtivos() {
        List<Emprestimo> ativos = new ArrayList<>();
        for (Emprestimo emp : historicoEmprestimos) {
            if (emp.getEstado() == EstadoEmprestimo.ATIVO || emp.getEstado() == EstadoEmprestimo.ATRASADO) {
                ativos.add(emp);
            }
        }
        return ativos;
    }

    /**
     * Calcula o total de multas pendentes de todos os empréstimos.
     *
     * @return total de multas em KZ
     */
    public double calcularTotalMultas() {
        double total = 0.0;
        for (Emprestimo emp : historicoEmprestimos) {
            total += emp.calcularMulta();
        }
        this.multaPendente = total;
        return total;
    }

    /**
     * Paga uma parte ou toda a multa pendente.
     *
     * @param valor o valor a pagar
     */
    public void pagarMulta(double valor) {
        if (valor > 0) {
            this.multaPendente = Math.max(0, this.multaPendente - valor);
        }
    }

    // === GETTERS E SETTERS ===

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public List<Emprestimo> getHistoricoEmprestimos() {
        return new ArrayList<>(historicoEmprestimos);
    }

    public void setHistoricoEmprestimos(List<Emprestimo> historicoEmprestimos) {
        this.historicoEmprestimos = historicoEmprestimos != null ? historicoEmprestimos : new ArrayList<>();
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public double getMultaPendente() {
        return multaPendente;
    }

    public void setMultaPendente(double multaPendente) {
        this.multaPendente = multaPendente;
    }

    @Override
    public String getIdentificador() {
        return this.matricula;
    }

    @Override
    public String toString() {
        return "Membro{id=" + id + ", nome='" + nome + "', matricula='" + matricula +
               "', empréstimosAtivos=" + numeroEmprestimosAtivos() +
               ", bloqueado=" + bloqueado + ", multaPendente=" + multaPendente + "}";
    }
}

