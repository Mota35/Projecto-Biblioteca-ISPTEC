package main.java.isptec.biblioteca.domain.entities;

import java.time.LocalDate;

public class Emprestimo {

    private int id;
    private Livro livro;
    private Estudante estudante;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataRealDevolucao;
    private int numeroRenovacoes;
    private EstadoEmprestimo estado;

    public Emprestimo(int id, Livro livro, Estudante estudante, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {

        this.id = id;
        this.livro = livro;
        this.estudante = estudante;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.numeroRenovacoes = 0;
        this.estado = EstadoEmprestimo.ATIVO;
    }

    public int getId() {
        return id;
    }

    public Livro getLivro() {
        return livro;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataRealDevolucao() {
        return dataRealDevolucao;
    }

    public void setDataRealDevolucao(LocalDate dataRealDevolucao) {
        this.dataRealDevolucao = dataRealDevolucao;
    }

    public int getNumeroRenovacoes() {
        return numeroRenovacoes;
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


}
