package main.java.isptec.biblioteca.service;

import main.java.isptec.biblioteca.domain.entities.Emprestimo;
import java.time.LocalDate;
import java.util.List;

public interface EmprestimoService {
    void registrarEmprestimo(Emprestimo emprestimo);
    void registrarDevolucao(int emprestimoId, LocalDate dataDevolucao);
    void renovarEmprestimo(int emprestimoId, LocalDate novaDataPrevistaDevolucao);
    Emprestimo buscarEmprestimoPorId(int emprestimoId);
    List<Emprestimo> listarEmprestimosAtivos();

}
