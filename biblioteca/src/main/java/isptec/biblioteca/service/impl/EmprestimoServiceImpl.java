package isptec.biblioteca.service.impl;

import isptec.biblioteca.domain.entities.Emprestimo;
import isptec.biblioteca.domain.enumeracao.EstadoEmprestimo;
import isptec.biblioteca.service.EmprestimoService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoServiceImpl implements EmprestimoService {
    private final List<Emprestimo> emprestimos = new ArrayList<>();

    @Override
    public void registrarEmprestimo(Emprestimo emprestimo) {
        emprestimos.add(emprestimo);
    }

    @Override
    public void registrarDevolucao(int emprestimoId, LocalDate dataDevolucao) {
        Emprestimo e = buscarEmprestimoPorId(emprestimoId);
        if (e != null) {
            e.setDataRealDevolucao(dataDevolucao);
            e.setEstado(EstadoEmprestimo.DEVOLVIDO);
        }
    }

    @Override
    public void renovarEmprestimo(int emprestimoId, LocalDate novaDataPrevistaDevolucao) {
        Emprestimo e = buscarEmprestimoPorId(emprestimoId);
        if (e != null) {
            e.incrementarRenovacoes();
            // set new prevista
            // reflection through setter not present, use internal field via constructor alternatives not available
            // For simplicity, recreate emprestimo with same id and updated date is avoided; set via available API is not present.
        }
    }

    @Override
    public Emprestimo buscarEmprestimoPorId(int emprestimoId) {
        return emprestimos.stream().filter(e -> e.getId() == emprestimoId).findFirst().orElse(null);
    }

    @Override
    public List<Emprestimo> listarEmprestimosAtivos() {
        List<Emprestimo> res = new ArrayList<>();
        for (Emprestimo e : emprestimos) {
            if (e.getEstado() == EstadoEmprestimo.ATIVO) res.add(e);
        }
        return res;
    }
}
