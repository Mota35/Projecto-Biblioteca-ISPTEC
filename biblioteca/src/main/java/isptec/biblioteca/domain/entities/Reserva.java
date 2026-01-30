package isptec.biblioteca.domain.entities;

import java.time.LocalDate;

public class Reserva {
    private int id;
    private Livro livro;
    private Estudante estudante;
    private LocalDate dataReserva;
    private boolean ativa;

    public Reserva(int id, Livro livro, Estudante estudante, LocalDate dataReserva) {
        this.id = id;
        this.livro = livro;
        this.estudante = estudante;
        this.dataReserva = dataReserva;
        this.ativa = true;
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

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }


}
