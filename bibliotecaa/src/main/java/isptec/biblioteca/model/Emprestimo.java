package isptec.biblioteca.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {
    private String id;
    private String livroId;
    private String membroId;
    private String tituloLivro;
    private String nomeMembro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private int numeroRenovacoes;
    private double multa;

    public Emprestimo(String id, String livroId, String membroId, 
                      String tituloLivro, String nomeMembro) {
        this.id = id;
        this.livroId = livroId;
        this.membroId = membroId;
        this.tituloLivro = tituloLivro;
        this.nomeMembro = nomeMembro;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = LocalDate.now().plusDays(14); // 14 dias de empréstimo
        this.numeroRenovacoes = 0;
        this.multa = 0.0;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLivroId() {
        return livroId;
    }

    public void setLivroId(String livroId) {
        this.livroId = livroId;
    }

    public String getMembroId() {
        return membroId;
    }

    public void setMembroId(String membroId) {
        this.membroId = membroId;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }

    public String getNomeMembro() {
        return nomeMembro;
    }

    public void setNomeMembro(String nomeMembro) {
        this.nomeMembro = nomeMembro;
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

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public int getNumeroRenovacoes() {
        return numeroRenovacoes;
    }

    public void setNumeroRenovacoes(int numeroRenovacoes) {
        this.numeroRenovacoes = numeroRenovacoes;
    }

    public double getMulta() {
        // Calcula automaticamente a multa sempre que for consultada
        calcularMulta();
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public boolean isAtivo() {
        return dataDevolucaoReal == null;
    }

    public boolean isAtrasado() {
        return isAtivo() && LocalDate.now().isAfter(dataDevolucaoPrevista);
    }

    public long getDiasAtraso() {
        if (!isAtrasado()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dataDevolucaoPrevista, LocalDate.now());
    }

    public void calcularMulta() {
        if (isAtrasado()) {
            this.multa = getDiasAtraso() * 2.0; // R$ 2,00 por dia
        }
    }

    public boolean podeRenovar() {
        return numeroRenovacoes < 2 && isAtivo();
    }

    public void renovar() {
        if (podeRenovar()) {
            this.numeroRenovacoes++;
            this.dataDevolucaoPrevista = this.dataDevolucaoPrevista.plusDays(14);
        }
    }
}