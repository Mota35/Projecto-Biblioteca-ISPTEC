package isptec.biblioteca.service;

import isptec.biblioteca.model.entities.Emprestimo;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.model.entities.Membro;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface de serviço para gestão de empréstimos.
 *
 * REGRAS DE NEGÓCIO:
 * - Um membro pode ter máximo 3 livros emprestados
 * - Livro só pode ser emprestado se estiver disponível
 * - Membro não pode estar bloqueado
 * - Prazo padrão: 14 dias
 * - Máximo 2 renovações
 * - Não pode renovar se existir reserva ativa para o livro
 * - Empréstimo atrasado gera multa diária
 */
public interface EmprestimoService {

    /**
     * Realiza o empréstimo de um livro para um membro.
     */
    boolean emprestarLivro(Membro membro, Livro livro);

    /**
     * Registra um empréstimo já criado.
     */
    void registrarEmprestimo(Emprestimo emprestimo);

    /**
     * Realiza a devolução de um livro.
     */
    void devolverLivro(Emprestimo emprestimo);

    /**
     * Realiza a devolução de um livro pelo ID do empréstimo.
     */
    void devolverLivro(int emprestimoId);

    /**
     * Realiza a devolução com data específica.
     */
    void devolverLivro(int emprestimoId, LocalDate dataDevolucao);

    /**
     * Renova um empréstimo.
     */
    boolean renovarEmprestimo(Emprestimo emprestimo);

    /**
     * Renova um empréstimo pelo ID.
     */
    boolean renovarEmprestimo(int emprestimoId);

    /**
     * Renova um empréstimo com nova data de devolução.
     */
    boolean renovarEmprestimo(int emprestimoId, LocalDate novaDataDevolucao);

    /**
     * Busca um empréstimo pelo ID.
     */
    Emprestimo buscarEmprestimoPorId(int emprestimoId);

    /**
     * Lista empréstimos ativos.
     */
    List<Emprestimo> listarEmprestimosAtivos();

    /**
     * Lista empréstimos atrasados.
     */
    List<Emprestimo> listarEmprestimosAtrasados();

    /**
     * Lista empréstimos de um membro.
     */
    List<Emprestimo> listarEmprestimosPorMembro(Membro membro);

    /**
     * Lista empréstimos de um livro.
     */
    List<Emprestimo> listarEmprestimosPorLivro(Livro livro);

    /**
     * Lista todos os empréstimos.
     */
    List<Emprestimo> listarTodosEmprestimos();

    /**
     * Calcula a multa de um empréstimo.
     */
    double calcularMulta(int emprestimoId);

    /**
     * Calcula o total de multas de um membro.
     */
    double calcularTotalMultasMembro(Membro membro);

    /**
     * Atualiza o estado de todos os empréstimos.
     */
    void atualizarEstadoEmprestimos();

    /**
     * Conta empréstimos ativos.
     */
    int contarEmprestimosAtivos();

    /**
     * Conta empréstimos atrasados.
     */
    int contarEmprestimosAtrasados();

    /**
     * Registra uma devolução.
     */
    void registrarDevolucao(int emprestimoId, LocalDate dataDevolucao);
}
