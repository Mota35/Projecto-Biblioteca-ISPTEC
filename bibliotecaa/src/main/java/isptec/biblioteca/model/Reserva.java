package isptec.biblioteca.model;

import java.time.LocalDate;
import isptec.biblioteca.enumeracao.StatusReserva;

public class Reserva {
    private String id;
    private String livroId;
    private String membroId;
    private String tituloLivro;
    private String nomeMembro;
    private LocalDate dataReserva;
    private StatusReserva status;

    

    public Reserva(String id, String livroId, String membroId, 
                   String tituloLivro, String nomeMembro) {
        this.id = id;
        this.livroId = livroId;
        this.membroId = membroId;
        this.tituloLivro = tituloLivro;
        this.nomeMembro = nomeMembro;
        this.dataReserva = LocalDate.now();
        this.status = StatusReserva.PENDENTE;
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

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public boolean isPendente() {
        return status == StatusReserva.PENDENTE;
    }

    public void concluir() {
        this.status = StatusReserva.CONCLUIDA;
    }

    public void cancelar() {
        this.status = StatusReserva.CANCELADA;
    }
}
