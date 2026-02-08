package isptec.biblioteca.service.impl;

import isptec.biblioteca.model.entities.Emprestimo;
import isptec.biblioteca.model.entities.Livro;
import isptec.biblioteca.model.entities.Membro;
import isptec.biblioteca.enumeracao.EstadoEmprestimo;
import isptec.biblioteca.service.EmprestimoService;
import isptec.biblioteca.service.ReservaService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de gestão de empréstimos.
 *
 * REGRAS DE NEGÓCIO IMPLEMENTADAS:
 * - Máximo 3 empréstimos por membro
 * - Livro deve estar disponível
 * - Membro não pode estar bloqueado
 * - Prazo padrão: 14 dias
 * - Máximo 2 renovações
 * - Não pode renovar se existir reserva ativa para o livro
 * - Multa de 50 KZ por dia de atraso
 */
public class EmprestimoServiceImpl implements EmprestimoService {

    private final List<Emprestimo> emprestimos = new ArrayList<>();
    private int nextId = 1;
    private ReservaService reservaService;

    public EmprestimoServiceImpl() {
    }

    public EmprestimoServiceImpl(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    public void setReservaService(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Override
    public boolean emprestarLivro(Membro membro, Livro livro) {
        // Validações
        if (membro == null || livro == null) {
            return false;
        }

        // Verifica se o membro pode emprestar (regra: máx 3 empréstimos, não bloqueado)
        if (!membro.podeEmprestar()) {
            return false;
        }

        // Verifica se o livro está disponível
        if (!livro.estaDisponivel()) {
            return false;
        }

        // Cria o empréstimo
        Emprestimo emprestimo = new Emprestimo(nextId++, livro, membro);

        // Realiza o empréstimo no livro (decrementa quantidade)
        if (!livro.emprestar()) {
            return false;
        }

        // Adiciona ao histórico do membro
        membro.adicionarEmprestimo(emprestimo);

        // Registra o empréstimo
        emprestimos.add(emprestimo);

        return true;
    }

    @Override
    public void registrarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo != null) {
            if (emprestimo.getId() == 0) {
                emprestimo.setId(nextId++);
            }
            emprestimos.add(emprestimo);
        }
    }

    @Override
    public void devolverLivro(int emprestimoId, LocalDate dataDevolucao) {
        Emprestimo emprestimo = buscarEmprestimoPorId(emprestimoId);
        if (emprestimo != null) {
            emprestimo.registrarDevolucao(dataDevolucao);

            // Devolve o livro (incrementa quantidade disponível)
            if (emprestimo.getLivro() != null) {
                emprestimo.getLivro().devolver();
            }
        }
    }

    @Override
    public void devolverLivro(int emprestimoId) {
        devolverLivro(emprestimoId, LocalDate.now());
    }

    @Override
    public void devolverLivro(Emprestimo emprestimo) {
        if (emprestimo != null) {
            devolverLivro(emprestimo.getId());
        }
    }

    @Override
    public boolean renovarEmprestimo(int emprestimoId) {
        Emprestimo emprestimo = buscarEmprestimoPorId(emprestimoId);
        if (emprestimo == null) {
            return false;
        }

        // Verifica se há reserva ativa para o livro
        if (reservaService != null && emprestimo.getLivro() != null) {
            if (reservaService.existeReservaAtivaParaLivro(emprestimo.getLivro())) {
                return false; // Não pode renovar se há reserva
            }
        }

        return emprestimo.renovar();
    }

    @Override
    public boolean renovarEmprestimo(int emprestimoId, LocalDate novaDataDevolucao) {
        Emprestimo emprestimo = buscarEmprestimoPorId(emprestimoId);
        if (emprestimo == null) {
            return false;
        }

        // Verifica se há reserva ativa para o livro
        if (reservaService != null && emprestimo.getLivro() != null) {
            if (reservaService.existeReservaAtivaParaLivro(emprestimo.getLivro())) {
                return false;
            }
        }

        return emprestimo.renovar(novaDataDevolucao);
    }

    @Override
    public boolean renovarEmprestimo(Emprestimo emprestimo) {
        if (emprestimo == null) {
            return false;
        }
        return renovarEmprestimo(emprestimo.getId());
    }

    @Override
    public Emprestimo buscarEmprestimoPorId(int emprestimoId) {
        return emprestimos.stream()
                .filter(e -> e.getId() == emprestimoId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimos.stream()
                .filter(e -> e.getEstado() == EstadoEmprestimo.ATIVO ||
                            e.getEstado() == EstadoEmprestimo.ATRASADO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Emprestimo> listarEmprestimosAtrasados() {
        atualizarEstadoEmprestimos();
        return emprestimos.stream()
                .filter(Emprestimo::estaAtrasado)
                .collect(Collectors.toList());
    }

    @Override
    public List<Emprestimo> listarEmprestimosPorMembro(Membro membro) {
        if (membro == null) return new ArrayList<>();
        return emprestimos.stream()
                .filter(e -> e.getMembro() != null && e.getMembro().getId() == membro.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Emprestimo> listarEmprestimosPorLivro(Livro livro) {
        if (livro == null) return new ArrayList<>();
        return emprestimos.stream()
                .filter(e -> e.getLivro() != null && e.getLivro().getId() == livro.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Emprestimo> listarTodosEmprestimos() {
        return new ArrayList<>(emprestimos);
    }

    @Override
    public double calcularMulta(int emprestimoId) {
        Emprestimo emprestimo = buscarEmprestimoPorId(emprestimoId);
        if (emprestimo == null) {
            return 0.0;
        }
        return emprestimo.calcularMulta();
    }

    @Override
    public double calcularTotalMultasMembro(Membro membro) {
        if (membro == null) return 0.0;
        return listarEmprestimosPorMembro(membro).stream()
                .mapToDouble(Emprestimo::calcularMulta)
                .sum();
    }

    @Override
    public void atualizarEstadoEmprestimos() {
        for (Emprestimo emp : emprestimos) {
            emp.atualizarEstado();
        }
    }

    @Override
    public int contarEmprestimosAtivos() {
        return (int) emprestimos.stream()
                .filter(e -> e.getEstado() == EstadoEmprestimo.ATIVO)
                .count();
    }

    @Override
    public int contarEmprestimosAtrasados() {
        atualizarEstadoEmprestimos();
        return (int) emprestimos.stream()
                .filter(Emprestimo::estaAtrasado)
                .count();
    }

    @Override
    public void registrarDevolucao(int emprestimoId, LocalDate dataDevolucao) {
        devolverLivro(emprestimoId, dataDevolucao);
    }
}
